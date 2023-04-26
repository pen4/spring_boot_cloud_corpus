package com.springboot.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.*;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;


@Configuration
@EnableIgniteRepositories(basePackages = "com.springboot.service")
public class IgniteConnConfig {
  /*  @Bean(name = "igniteInstance")
    public Ignite igniteInstance() {

        return Ignition.start(igniteCfg());
    }
    @Bean(name = "igniteCfg")
    public IgniteConfiguration igniteCfg(){
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        ConnectorConfiguration connectorCfg = new ConnectorConfiguration();
        connectorCfg.setPort(10800);
        connectorCfg.setHost("192.168.31.237");
        igniteConfiguration.setConnectorConfiguration(connectorCfg);


        igniteConfiguration.setClientMode(true);
        igniteConfiguration.setMetricsLogFrequency(0);
        return igniteConfiguration;
    }*/

    @Bean
    public IgniteClient igniteInstance() {

       return   Ignition
                .startClient(new ClientConfiguration()
                        .setAddresses("192.168.31.237"));


    }


}