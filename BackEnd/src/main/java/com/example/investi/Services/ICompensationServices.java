package com.example.investi.Services;



import com.example.investi.Entities.Compensation;

import java.util.List;
import java.util.Optional;

public interface ICompensationServices {

    Compensation createCompensation(Compensation compensation);

    List<Compensation> getAllCompensations();

    Optional<Compensation> getCompensationById(Long id);

    Compensation updateCompensation(Compensation compensation);

    void deleteCompensation(Long id);
}
