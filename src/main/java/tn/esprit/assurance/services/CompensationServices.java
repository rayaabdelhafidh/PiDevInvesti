package tn.esprit.assurance.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tn.esprit.assurance.entity.Compensation;
import tn.esprit.assurance.repositories.CompensationRepository;
import tn.esprit.assurance.services.ICompensationServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component

public class CompensationServices implements ICompensationServices {
@Autowired
    private  CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(Compensation compensation) {
        return compensationRepository.save(compensation);
    }

    @Override
    public List<Compensation> getAllCompensations() {
        return compensationRepository.findAll();
    }

    @Override
    public Optional<Compensation> getCompensationById(Long id) {
        return compensationRepository.findById(id);
    }

    @Override
    public Compensation updateCompensation( Compensation newCompensation) {
        return compensationRepository.save(newCompensation);

    }

    @Override
    public void deleteCompensation(Long id) {
        compensationRepository.deleteById(id);
    }
    @Scheduled(cron = "0 0 0 * * ?") // Exécuter chaque jour à minuit
   //@Scheduled(fixedRate = 10000) // Exécute la tâche toutes les 10 secondes

    public void updateCompensationStatus() {
        List<Compensation> compensations = compensationRepository.findAll();

        for (Compensation compensation : compensations) {
            if (compensation.getCompensationDate().isBefore(LocalDate.now()) ||
                    compensation.getCompensationDate().isEqual(LocalDate.now())) {

                // Mise à jour du statut
                compensation.setStatus(Compensation.CompensationStatus.PAYE);
                compensationRepository.save(compensation);
            }
        }
    }
}
