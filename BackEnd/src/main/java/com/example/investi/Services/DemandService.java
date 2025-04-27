package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.*;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class DemandService implements IDemandService {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private EmailService emailService;
    @Autowired
    ILoanRepo loanRepo;
    @Autowired
    private IDemandRepo demandRepository;
    @Autowired
    IPackRepo PackRepo;
    @Autowired
    ClientRepository userRepo;
    @Autowired
    ICollateralRepo collateralRepo;

    @Autowired
    private ICollateralRepo collateralRepository;

    private static final String DOCUMENTS_DIR = "path/to/storage/directory/";

    @Override
    public Demand AddDemand(Demand demand, Long packId,Long UserId) {
        Pack pack = PackRepo.findById(packId).get();
        Client user=userRepo.findById(UserId).get();
        demand.setPack(pack);
        demand.setUser(user);
        demand.setStatus(DemandStatus.EnAttente);
        return demandRepository.save(demand);
    }

    @Override
    public List<Demand> getAllDemands() {
        return demandRepository.findAll();
    }

    @Override
    public Optional<Demand> getDemandById(Long DemandId) {
        return demandRepository.findById(DemandId);
    }

    @Override
    public Demand UpdateDemand(Long DemandId, Demand demand) {
        Optional<Demand> existingDemandOpt = demandRepository.findById(DemandId);

        if (existingDemandOpt.isPresent()) {
            Demand existingDemand = existingDemandOpt.get();
            existingDemand.setDemandDate(demand.getDemandDate());
            existingDemand.setAmount(demand.getAmount());
            existingDemand.setDuration(demand.getDuration());
            existingDemand.setStatus(demand.getStatus());

            return demandRepository.save(existingDemand);
        }
        return null;
    }

    @Override
    public void DeleteDemand(Long DemandId) {
        demandRepository.deleteById(DemandId);
    }


    @Transactional
    @Override
    public void TraiterDemande(Long demandId) {
        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        try {
            String userEmail = demand.getUser().getEmail();
            int score = calculerCreditScore(demand.getUser());
            System.out.println("Score du client : " + score + "/100");

            if (score < 30) {
                throw new RuntimeException("Demande refusée : score insuffisant (" + score + "/100)");
            }


            // Règle 1 : Vérifier si le client est éligible (exemple basique)
            if (!isClientEligible(demand.getUser().getId())) {
                throw new RuntimeException("Le client n'est pas éligible.");
            }

            // Règle 2 : Vérifier si le client a déjà un prêt actif
            if (loanRepo.existsActiveLoanByUserId(demand.getUser().getId(), LoanStatut.ENCOURS)) {
                throw new RuntimeException("Le client a déjà un prêt en cours.");
            }

            // Règle 3 : Vérifier le montant par rapport au Pack
            Pack pack = demand.getPack();
            if (!isAmountWithinInterval(demand.getAmount(), pack.getIntervalle())) {
                throw new RuntimeException("Montant de la demande hors de l'intervalle du pack (" + pack.getIntervalle() + ").");
            }

            // Règle 4 : Vérifier s'il a une garantie suffisante ou un garant
            if (!hasValidGuaranteeOrGarant(demand.getUser(), demand.getAmount())) {
                throw new RuntimeException("Le client n'a ni garantie suffisante ni garant.");
            }

            demand.setStatus(DemandStatus.ACCEPTE);
            System.out.println("aaaaaaaaaaaaaa");
            demandRepository.save(demand);
            //  emailService.sendEmail(
            //        userEmail,
            //      "Demande acceptée",
            //    "Félicitations ! Votre demande a été acceptée et votre prêt a été créé.");
            //pdfService.generateProfessionalContractPdf(demand);


            // 1. Générer le PDF du contrat
            pdfService.generateProfessionalContractPdf(demand);

// 2. L'URL de ta page promo  http://192.168.0.199/promo/index.html
            String promoPageUrl = "https://about.me/investi";

// 3. Générer l'image du QR code contenant l'URL de la page promo
            String qrCodeImagePath = EmailService.QrCodeGenerator.generateQrCodeImage(promoPageUrl);

// 4. Envoyer l'email avec le QR code en pièce jointe
            emailService.sendEmailWithQrCode(
                    demand.getUser().getEmail(),
                    "Votre demande acceptée - Offre spéciale",
                    "Votre demande a été acceptée ! Scannez ce QR code pour découvrir notre offre spéciale.",
                    qrCodeImagePath
            );
            if ( demand.getStatus().equals(DemandStatus.ACCEPTE)) {
                Loan loan = new Loan();
                loan.setDateDebut(new Date());
                loan.setInterestRate(5F);  //5% annuel
                loan.setDemand(demand);
                loan.setStatus(LoanStatut.ENCOURS) ;


                float montantDemande = demand.getAmount();
                int dureeMois = demand.getDuration();
                float dureeAnnees = dureeMois / 12f;
                float tauxInteret = loan.getInterestRate() / 100;
                float interetTotal = montantDemande * tauxInteret * dureeAnnees;
                //  (principal + intérêts)
                float montantTotalARembourser = montantDemande + interetTotal;
                loan.setAmount(montantTotalARembourser);

                generateInstallments(loan);

                loanRepo.save(loan);
            }
            if(demand.getStatus().equals(DemandStatus.REFUSE)) {
                demand.getUser().setTotalRefusedDemands(demand.getUser().getTotalRefusedDemands()+1);
                emailService.sendEmail(
                        userEmail,
                        "Demande refusée",
                        "Désolé, votre demande a été refusée.");
            }

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status invalide : " + demand.getStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateInstallments(Loan loan) {
        int totalMonths = loan.getDemand().getDuration();
        Float monthlyPayment = calculateMonthlyPayment(loan);

        List<Installement> installments = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loan.getDateDebut());

        for (int i = 0; i < totalMonths; i++) {
            Installement installment = new Installement();
            installment.setDateEcheance(calendar.getTime());
            installment.setAmount(monthlyPayment);
            installment.setLoan(loan); // Liaison avec le prêt
            installments.add(installment);

            calendar.add(Calendar.MONTH, 1); // Passer au mois suivant
        }

        loan.setInstallments(installments); // Liaison des tranches au prêt
    }


    private Float calculateMonthlyPayment(Loan loan) {
        float montant = loan.getAmount();
        float tauxAnnuel = loan.getInterestRate();
        int dureeMois = loan.getDemand().getDuration();

        float tauxMensuel = tauxAnnuel / 12 / 100;
        return (montant * tauxMensuel) / (1 - (float)Math.pow(1 + tauxMensuel, -dureeMois));
    }

    public int calculerCreditScore(Client user) {
        int score = 0;

        // Critère 1 : Revenu mensuel
        if (user.getRevenuMensuel() != null && user.getRevenuMensuel() >= 1000) {
            score += 20;
        }

        // Critère 2 : Score crédit
        if (user.getScoreCredit() != null && user.getScoreCredit() >= 70) {
            score += 20;
        }

        // Critère 3 : Retards de prêts
        if (user.getTotalLateLoans() != null && user.getTotalLateLoans() < 2) {
            score += 20;
        }

        // Critère 4 : Refus récents
        if (user.getTotalRefusedDemands() != null && user.getTotalRefusedDemands() < 3) {
            score += 20;
        }

        // Critère 5 : Ancienneté bancaire
        if (ancienneteBancaireEnAnnees(user) >= 2) {
            score += 20;
        }
        user.setScoreCredit(score);
        return score;
    }


    public int ancienneteBancaireEnAnnees(Client user) {
        LocalDate dateInscription = user.getDateInscription().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(dateInscription, LocalDate.now()).getYears();
    }



    public boolean isClientEligible(long userId) {
        Client user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Critère 1 : Âge minimum
        if (user.getDateOfBirth() != null) {
            LocalDate userDateOfBirth = user.getDateOfBirth().toLocalDate(); // Convert LocalDateTime to LocalDate
            int age = Period.between(userDateOfBirth, LocalDate.now()).getYears();
            if (age < 18) {
                return false;
            }
        }

        // Critère 2 : Nombre de demandes refusées
        int refusedDemandsCount = demandRepository.countByUserIdAndStatus(userId, DemandStatus.REFUSE);
        if (refusedDemandsCount >= 3) {
            return false;
        }
        return true;
    }

    private boolean hasValidGuaranteeOrGarant(Client user, Float amount) {
        List<Collateral> garanties = collateralRepo.findByUserId(user.getId());

        for (Collateral garantie : garanties) {
            if (garantie.getType() == CollateralType.GARANT) {
                return true;
            }
            if (garantie.getValue() != null && garantie.getValue() >= amount *  0.5) {
                return true;
            }
        }
        return false;
    }


    private boolean isAmountWithinInterval(float amount, String intervalle) {
        try {
            String[] parts = intervalle.split("-");
            float min = Float.parseFloat(parts[0]);
            float max = Float.parseFloat(parts[1]);
            return amount >= min && amount <= max;
        } catch (Exception e) {
            throw new RuntimeException("Intervalle du pack invalide : " + intervalle);
        }
    }





}