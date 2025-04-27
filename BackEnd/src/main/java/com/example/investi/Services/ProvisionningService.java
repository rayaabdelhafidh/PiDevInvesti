package com.example.investi.Services;


import com.example.investi.Entities.Contract;
import com.example.investi.Repositories.ContractRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ProvisionningService  {

    @Autowired
    private ContractRepository contractRepository;

    private static final Random random = new Random();
    private static final String FILE_PATH = "C:\\Users\\Public\\ProvisioningReport.xlsx";
    public void generateProvisioningReport() throws IOException {
        // Filter active contracts using ContractStatus enum
        List<Contract> contracts = contractRepository.findAll();
        List<Contract> activeContracts = new ArrayList<>();
        for (Contract contract : contracts) {
            if (contract.getStatus() == Contract.ContractStatus.ACTIVE) {  // Checking if the status is ACTIVE
                activeContracts.add(contract);
            }
        }

        // Proceed with provisioning calculations using only active contracts
        Map<Integer, Double> primesRegroupees = calculerPrimesRegroupees(activeContracts);
        Map<Integer, Double> primesAcquises = calculerPrimesAcquises(primesRegroupees);
        Map<Integer, Double[]> paiementsNonCumules = calculerPaiementsNonCumules(primesRegroupees);
        Map<Integer, Double> paiementsCumules = calculerPaiementsCumules(paiementsNonCumules);
        Map<Integer, Double[]> coutsEtPSAP = calculerCoutsEtPSAP(primesAcquises, paiementsCumules);

        // Création du fichier Excel
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {

            Sheet sheet = workbook.createSheet("Provisioning Report");

            // Écriture des données dans le fichier Excel
            int rowNum = 0;
            rowNum = ecrireTableau(sheet, rowNum, "Primes regroupées par année:", primesRegroupees);
            rowNum = ecrireTableau(sheet, rowNum, "Primes acquises:", primesAcquises);
            rowNum = ecrireTableauPaiementsNonCumules(sheet, rowNum, "Paiements non cumulés:", paiementsNonCumules);
            rowNum = ecrireTableau(sheet, rowNum, "Paiements cumulés:", paiementsCumules);
            ecrireTableauCoutsEtPSAP(sheet, rowNum, "Coûts ultimes, paiements et PSAP:", coutsEtPSAP);

            // Enregistrement du fichier Excel
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception or handle it as needed
        }
    }


    private Map<Integer, Double> calculerPrimesRegroupees(List<Contract> contracts) {
        Map<Integer, Double> primesRegroupees = new HashMap<>();
        for (Contract contract : contracts) {
            int year = contract.getStartDate().getYear();
            double premium = contract.getAnnualPremium();
            primesRegroupees.put(year, primesRegroupees.getOrDefault(year, 0.0) + premium);
        }
        return primesRegroupees;
    }

    private Map<Integer, Double> calculerPrimesAcquises(Map<Integer, Double> primesRegroupees) {
        Map<Integer, Double> primesAcquises = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : primesRegroupees.entrySet()) {
            int year = entry.getKey();
            double primes = entry.getValue();
            double sip = 0.6 + random.nextDouble() * 0.2; // SIP aléatoire entre 60% et 80%
            double primesAcquisesAnnee = primes * sip;
            primesAcquises.put(year, primesAcquisesAnnee);
        }
        return primesAcquises;
    }

    private Map<Integer, Double[]> calculerPaiementsNonCumules(Map<Integer, Double> primesRegroupees) {
        Map<Integer, Double[]> paiementsNonCumules = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : primesRegroupees.entrySet()) {
            int year = entry.getKey();
            double primes = entry.getValue();
            List<Double> paiementsList = new ArrayList<>();
            double primesRestantes = primes;
            double pourcentage = 0.15; // Pourcentage initial de paiement (15%)

            while (primesRestantes > 0 && pourcentage > 0.01) { // Tant qu'il reste des primes et que le pourcentage est > 1%
                double paiement = primes * pourcentage;
                paiementsList.add(paiement);
                primesRestantes -= paiement;
                pourcentage -= 0.02; // Réduire le pourcentage de 2% à chaque année
            }

            Double[] paiements = paiementsList.toArray(new Double[0]);
            paiementsNonCumules.put(year, paiements);
        }
        return paiementsNonCumules;
    }

    private Map<Integer, Double> calculerPaiementsCumules(Map<Integer, Double[]> paiementsNonCumules) {
        Map<Integer, Double> paiementsCumules = new HashMap<>();
        for (Map.Entry<Integer, Double[]> entry : paiementsNonCumules.entrySet()) {
            int year = entry.getKey();
            Double[] paiements = entry.getValue();
            double cumul = 0;
            for (double paiement : paiements) {
                cumul += paiement;
            }
            paiementsCumules.put(year, cumul);
        }
        return paiementsCumules;
    }

    private Map<Integer, Double[]> calculerCoutsEtPSAP(Map<Integer, Double> primesAcquises, Map<Integer, Double> paiementsCumules) {
        Map<Integer, Double[]> coutsEtPSAP = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : primesAcquises.entrySet()) {
            int year = entry.getKey();
            double primes = entry.getValue();
            double paiements = paiementsCumules.getOrDefault(year, 0.0);
            double psap = primes - paiements;
            coutsEtPSAP.put(year, new Double[]{primes, paiements, psap});
        }
        return coutsEtPSAP;
    }

    private int ecrireTableau(Sheet sheet, int rowNum, String titre, Map<Integer, Double> tableau) {
        Row titreRow = sheet.createRow(rowNum++);
        Cell titreCell = titreRow.createCell(0);
        titreCell.setCellValue(titre);

        for (Map.Entry<Integer, Double> entry : tableau.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(entry.getKey());
            Cell cell2 = row.createCell(1);
            cell2.setCellValue(entry.getValue());
        }
        return rowNum;
    }

    private int ecrireTableauPaiementsNonCumules(Sheet sheet, int rowNum, String titre, Map<Integer, Double[]> tableau) {
        Row titreRow = sheet.createRow(rowNum++);
        Cell titreCell = titreRow.createCell(0);
        titreCell.setCellValue(titre);

        for (Map.Entry<Integer, Double[]> entry : tableau.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(entry.getKey());
            Double[] paiements = entry.getValue();
            for (int i = 0; i < paiements.length; i++) {
                Cell cell = row.createCell(i + 1);
                cell.setCellValue(paiements[i]);
            }
        }
        return rowNum;
    }

    private void ecrireTableauCoutsEtPSAP(Sheet sheet, int rowNum, String titre, Map<Integer, Double[]> tableau) {
        Row titreRow = sheet.createRow(rowNum++);
        Cell titreCell = titreRow.createCell(0);
        titreCell.setCellValue(titre);

        for (Map.Entry<Integer, Double[]> entry : tableau.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(entry.getKey());
            Double[] valeurs = entry.getValue();
            Cell cell2 = row.createCell(1);
            cell2.setCellValue(valeurs[0]);
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(valeurs[1]);
            Cell cell4 = row.createCell(3);
            cell4.setCellValue(valeurs[2]);
        }
    }
}