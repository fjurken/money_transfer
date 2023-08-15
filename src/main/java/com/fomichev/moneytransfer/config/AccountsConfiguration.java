package com.fomichev.moneytransfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Configuration class provides account list from application.yaml
 */
@Configuration
@RefreshScope
public class AccountsConfiguration {

    @Value("#{${accountsFrom}}")
    public Map<Long, Double> accountsFrom;

    @Value("#{${accountsTo}}")
    public List<Long> accountsTo;

    @Bean
    AccountsConfiguration accountsConfig() {
        return new AccountsConfiguration();
    }

}
