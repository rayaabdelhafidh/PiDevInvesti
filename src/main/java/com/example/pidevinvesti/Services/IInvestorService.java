package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.Investor;

public interface IInvestorService <Investor, ID>{

    Investor getInvestorById(ID id);

}
