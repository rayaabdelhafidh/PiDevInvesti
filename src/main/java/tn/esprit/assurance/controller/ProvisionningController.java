package tn.esprit.assurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.assurance.services.ProvisionningService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/provisioning")
public class ProvisionningController {

    @Autowired
    private ProvisionningService provisioningService;

    private static final String FILE_PATH = "C:\\Users\\Public\\ProvisioningReport.xlsx";

    @GetMapping("/generateReport")
    public ResponseEntity<FileSystemResource> generateProvisioningReport() throws IOException {
        try {
            provisioningService.generateProvisioningReport();
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return ResponseEntity.internalServerError().body(null);
            }

            FileSystemResource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProvisioningReport.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        }  catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to generate the provisioning report: " + e.getMessage());
        }

    }
}
