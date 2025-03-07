package tn.esprit.assurance.services;

import tn.esprit.assurance.entity.Compensation;

import java.util.List;
import java.util.Optional;

public interface ICompensationServices {

    Compensation createCompensation(Compensation compensation);

    List<Compensation> getAllCompensations();

    Optional<Compensation> getCompensationById(Long id);

    Compensation updateCompensation(Compensation compensation);

    void deleteCompensation(Long id);
}
