package com.example.investi.Services;

public interface IInvestorService <Investor, ID>{

    Investor getInvestorById(ID id);

}
