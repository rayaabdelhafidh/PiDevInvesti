package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.DemandStatus;
import com.example.pidevinvesti.Entity.Loan;
import com.example.pidevinvesti.Entity.Pack;
import com.example.pidevinvesti.Repository.ICollateralRepo;
import com.example.pidevinvesti.Repository.IDemandRepo;
import com.example.pidevinvesti.Repository.ILoanRepo;
import com.example.pidevinvesti.Repository.IPackRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandService implements IDemandService {



    @Autowired
    ILoanRepo loanRepo;
    @Autowired
    private IDemandRepo demandRepository;
    @Autowired
    IPackRepo PackRepo;

    @Autowired
    private ICollateralRepo collateralRepository;

    private static final String DOCUMENTS_DIR = "path/to/storage/directory/";

    @Override
    public Demand AddDemand(Demand demand, Long packId) {
        Pack pack = PackRepo.findById(packId).get();
        demand.setPack(pack);
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


    @Override
    @Transactional
    public void TraiterDemande(Long demandId, String status) {
        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        try {
            DemandStatus demandStatus = DemandStatus.valueOf(status.toUpperCase());
            demand.setStatus(demandStatus);
            demandRepository.save(demand);

            if (demandStatus == DemandStatus.ACCEPTE) {
                Loan loan = new Loan();
                loan.setDateDebut(new Date());
                loan.setAmount(demand.getAmount());
                loan.setInterestRate(5F); // Taux d'intérêt fixé à 5%
                loan.setDemand(demand);
                loanRepo.save(loan);
            }

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status invalide : " + status);
        }
    }


}
