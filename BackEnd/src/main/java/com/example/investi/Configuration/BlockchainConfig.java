package com.example.investi.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class BlockchainConfig {

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService("HTTP://0.0.0.0:7545"));
    }
}