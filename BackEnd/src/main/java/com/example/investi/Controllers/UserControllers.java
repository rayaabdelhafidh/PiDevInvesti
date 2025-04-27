package com.example.investi.Controllers;


import com.example.investi.Entities.*;
import com.example.investi.Repositories.UserRepository;
import com.example.investi.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientServices clientService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private InvestorService investorService;

    @Autowired
    private AssureurServices assureurService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a new user based on the provided role.
     *
     * @param payload A map containing user data and the role.
     * @return The created user.
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody Map<String, Object> payload) {
        String role = (String) payload.get("role");
        Map<String, Object> userMap = (Map<String, Object>) payload.get("user");

        // Create the appropriate user type based on the role
        User user;
        switch (role.toUpperCase()) {
            case "CLIENT":
                user = new Client();
                Map<String, Object> client = (Map<String, Object>) payload.get("clientDetails");

                // Validate and set Client-specific fields
                String dateOfBirthStr = (String) client.get("dateOfBirth");
                if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) {
                    throw new IllegalArgumentException("Date of birth is required for a client.");
                }
                ((Client) user).setDateOfBirth(LocalDateTime.parse(dateOfBirthStr));

                ((Client) user).setGender((String) client.get("gender"));
                ((Client) user).setProfession((String) client.get("profession"));
                ((Client) user).setPreferredLanguage((String) client.get("preferredLanguage"));
                ((Client) user).setNationalId((String) client.get("nationalId"));

                // Optional fields with null checks
                if (client.get("revenuMensuel") != null) {
                    ((Client) user).setRevenuMensuel(Float.parseFloat(client.get("revenuMensuel").toString()));
                }
                if (client.get("scoreCredit") != null) {
                    ((Client) user).setScoreCredit(Integer.parseInt(client.get("scoreCredit").toString()));
                }
                if (client.get("dateInscription") != null) {
                    Object dateInscriptionValue = client.get("dateInscription");
                    long timestamp;

                    if (dateInscriptionValue instanceof String) {
                        timestamp = Long.parseLong((String) dateInscriptionValue);
                    } else if (dateInscriptionValue instanceof Number) {
                        timestamp = ((Number) dateInscriptionValue).longValue();
                    } else {
                        throw new IllegalArgumentException("Invalid type for dateInscription. Expected String or numeric value.");
                    }

                    ((Client) user).setDateInscription(new Date(timestamp));
                }
                break;

            case "INVESTOR":
                user = new Investor();
                Map<String, Object> investor = (Map<String, Object>) payload.get("investorDetails");

                // Validate and set Investor-specific fields
                if (investor.get("investamount") == null) {
                    throw new IllegalArgumentException("Investment amount is required for an investor.");
                }
                ((Investor) user).setInvestamount(Double.parseDouble(investor.get("investamount").toString()));

                if (investor.get("description") != null) {
                    ((Investor) user).setDescription((String) investor.get("description"));
                }

                if (investor.get("investorStatus") != null) {
                    ((Investor) user).setInvestorStatus(InvestorStatus.valueOf((String) investor.get("investorStatus")));
                }

                if (investor.get("investmentdate") != null) {
                    String investmentDateStr = (String) investor.get("investmentdate");
                    ((Investor) user).setInvestmentdate(LocalDateTime.parse(investmentDateStr));
                }

                if (investor.get("riskTolerance") != null) {
                    ((Investor) user).setRiskTolerance(RiskProfile.valueOf((String) investor.get("riskTolerance")));
                }

                if (investor.get("preferredSectors") != null) {
                    ((Investor) user).setPreferredSectors((String) investor.get("preferredSectors"));
                }
                break;

            case "TRAINER":
                user = new Trainer();
                Map<String, Object> trainer = (Map<String, Object>) payload.get("trainerDetails");

                // Validate and set Trainer-specific fields
                if (trainer.get("specialization") == null) {
                    throw new IllegalArgumentException("Specialization is required for a trainer.");
                }
                ((Trainer) user).setSpecialization((String) trainer.get("specialization"));

                if (trainer.get("experienceYears") != null) {
                    ((Trainer) user).setExperienceYears(Integer.parseInt(trainer.get("experienceYears").toString()));
                }

                if (trainer.get("certifications") != null) {
                    ((Trainer) user).setCertifications((String) trainer.get("certifications"));
                }

                if (trainer.get("photoUrl") != null) {
                    ((Trainer) user).setPhotoUrl((String) trainer.get("photoUrl"));
                }
                break;

            case "ASSUREUR":
                user = new Assureur();
                // Handle Assureur-specific fields here
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        // Set common fields for all user types
        user.setFirstName((String) userMap.get("firstName"));
        user.setLastName((String) userMap.get("lastName"));
        user.setEmail((String) userMap.get("email"));
        user.setPassword((String) userMap.get("password"));
        user.setAdresse((String) userMap.get("adresse"));
        user.setPhonenumber((String) userMap.get("phonenumber"));

        // Save the user using the appropriate service
        switch (role.toUpperCase()) {
            case "CLIENT":
                return ResponseEntity.ok(clientService.AddClient((Client) user));
            case "INVESTOR":
                return ResponseEntity.ok(investorService.addInvestor((Investor) user));
            case "TRAINER":
                return ResponseEntity.ok(trainerService.AddTrainer((Trainer) user));
            case "ASSUREUR":
                return ResponseEntity.ok(assureurService.AddAssureur((Assureur) user));
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        String role = (String) payload.get("role");
        Map<String, Object> userMap = (Map<String, Object>) payload.get("user");

        // Fetch the existing user based on ID and role
        User user;
        switch (role.toUpperCase()) {
            case "CLIENT":
                user = clientService.getClientById(id);
                Map<String, Object> clientDetails = (Map<String, Object>) payload.get("clientDetails");

                if (clientDetails != null) {
                    if (clientDetails.get("dateOfBirth") != null) {
                        ((Client) user).setDateOfBirth(LocalDateTime.parse((String) clientDetails.get("dateOfBirth")));
                    }
                    if (clientDetails.get("gender") != null) {
                        ((Client) user).setGender((String) clientDetails.get("gender"));
                    }
                    if (clientDetails.get("profession") != null) {
                        ((Client) user).setProfession((String) clientDetails.get("profession"));
                    }
                    if (clientDetails.get("preferredLanguage") != null) {
                        ((Client) user).setPreferredLanguage((String) clientDetails.get("preferredLanguage"));
                    }
                    if (clientDetails.get("nationalId") != null) {
                        ((Client) user).setNationalId((String) clientDetails.get("nationalId"));
                    }
                    if (clientDetails.get("revenuMensuel") != null) {
                        ((Client) user).setRevenuMensuel(Float.parseFloat(clientDetails.get("revenuMensuel").toString()));
                    }
                    if (clientDetails.get("scoreCredit") != null) {
                        ((Client) user).setScoreCredit(Integer.parseInt(clientDetails.get("scoreCredit").toString()));
                    }
                    if (clientDetails.get("dateInscription") != null) {
                        Object dateInscriptionValue = clientDetails.get("dateInscription");
                        long timestamp;

                        if (dateInscriptionValue instanceof String) {
                            timestamp = Long.parseLong((String) dateInscriptionValue);
                        } else if (dateInscriptionValue instanceof Number) {
                            timestamp = ((Number) dateInscriptionValue).longValue();
                        } else {
                            throw new IllegalArgumentException("Invalid type for dateInscription. Expected String or numeric value.");
                        }

                        ((Client) user).setDateInscription(new Date(timestamp));
                    }
                }
                break;

            case "INVESTOR":
                user = investorService.GetInvestorById(id);
                Map<String, Object> investorDetails = (Map<String, Object>) payload.get("investorDetails");

                if (investorDetails != null) {
                    if (investorDetails.get("investamount") != null) {
                        ((Investor) user).setInvestamount(Double.parseDouble(investorDetails.get("investamount").toString()));
                    }
                    if (investorDetails.get("description") != null) {
                        ((Investor) user).setDescription((String) investorDetails.get("description"));
                    }
                    if (investorDetails.get("investorStatus") != null) {
                        ((Investor) user).setInvestorStatus(InvestorStatus.valueOf((String) investorDetails.get("investorStatus")));
                    }
                    if (investorDetails.get("investmentdate") != null) {
                        ((Investor) user).setInvestmentdate(LocalDateTime.parse((String) investorDetails.get("investmentdate")));
                    }
                    if (investorDetails.get("riskTolerance") != null) {
                        ((Investor) user).setRiskTolerance(RiskProfile.valueOf((String) investorDetails.get("riskTolerance")));
                    }
                    if (investorDetails.get("preferredSectors") != null) {
                        ((Investor) user).setPreferredSectors((String) investorDetails.get("preferredSectors"));
                    }
                }
                break;

            case "TRAINER":
                user = trainerService.GetTrainerById(id);
                Map<String, Object> trainerDetails = (Map<String, Object>) payload.get("trainerDetails");

                if (trainerDetails != null) {
                    if (trainerDetails.get("specialization") != null) {
                        ((Trainer) user).setSpecialization((String) trainerDetails.get("specialization"));
                    }
                    if (trainerDetails.get("experienceYears") != null) {
                        ((Trainer) user).setExperienceYears(Integer.parseInt(trainerDetails.get("experienceYears").toString()));
                    }
                    if (trainerDetails.get("certifications") != null) {
                        ((Trainer) user).setCertifications((String) trainerDetails.get("certifications"));
                    }
                    if (trainerDetails.get("photoUrl") != null) {
                        ((Trainer) user).setPhotoUrl((String) trainerDetails.get("photoUrl"));
                    }
                }
                break;

            case "ASSUREUR":
                user = assureurService.GetAssureurById(id);
                // Handle Assureur-specific fields here
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        // Update common fields
        if (userMap.get("firstName") != null) {
            user.setFirstName((String) userMap.get("firstName"));
        }
        if (userMap.get("lastName") != null) {
            user.setLastName((String) userMap.get("lastName"));
        }
        if (userMap.get("email") != null) {
            user.setEmail((String) userMap.get("email"));
        }
        if (userMap.get("password") != null) {
            user.setPassword((String) userMap.get("password"));
        }
        if (userMap.get("adresse") != null) {
            user.setAdresse((String) userMap.get("adresse"));
        }
        if (userMap.get("phonenumber") != null) {
            user.setPhonenumber((String) userMap.get("phonenumber"));
        }

        // Save the updated user
        switch (role.toUpperCase()) {
            case "CLIENT":
                return ResponseEntity.ok(clientService.updateClient((Client) user));
            case "INVESTOR":
                return ResponseEntity.ok(investorService.updateInvestor((Investor) user));
            case "TRAINER":
                return ResponseEntity.ok(trainerService.UpdateTrainer((Trainer) user));
            case "ASSUREUR":
                return ResponseEntity.ok(assureurService.updateAssureur((Assureur) user));
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestParam String role) {
        switch (role.toUpperCase()) {
            case "CLIENT":
                clientService.deleteClient(id);
                break;
            case "INVESTOR":
                investorService.deleteInvestor(id);
                break;
            case "TRAINER":
                trainerService.DeleteTrainer(id);
                break;
            case "ASSUREUR":
                assureurService.deleteAssureur(id);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/list")
    public ResponseEntity<List<? extends User>> listUsersByRole(@RequestParam String role) {
        List<? extends User> users;
        switch (role.toUpperCase()) {
            case "CLIENT":
                users = clientService.getAllClients();
                break;
            case "INVESTOR":
                users = investorService.getAllInvestors();
                break;
            case "TRAINER":
                users = trainerService.GetAllTrainers();
                break;
            case "ASSUREUR":
                users = assureurService.getAllAssureurs();
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role")
    public ResponseEntity<String> getUserRoleByEmail(@RequestParam String email) {
        User user = (User) userRepository.findByEmail(email).get();

        // Extract the user type (e.g., "CLIENT", "INVESTOR") from the discriminator column
        String userType = user.getClass().getSimpleName().toUpperCase();
        String role = "ROLE_" + userType;

        return ResponseEntity.ok(role);
    }
}