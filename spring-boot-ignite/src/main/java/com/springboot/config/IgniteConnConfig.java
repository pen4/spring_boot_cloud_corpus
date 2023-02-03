package com.springboot.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.ClientTransactionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.LoadAllWarmUpConfiguration;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class IgniteConnConfig {
  /*  @Bean(name = "igniteInstance")
    public Ignite igniteInstance(Ignite ignite) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        ClientConnectorConfiguration clientConnectorConfiguration = new ClientConnectorConfiguration()
                .setThinClientEnabled(true).setHost("192.168.31.237").setPort(10800);
        igniteConfiguration.setClientConnectorConfiguration(clientConnectorConfiguration);
        igniteConfiguration.setClientMode(true);

        return Ignition.start(igniteConfiguration);
    }*/

    @Bean
    public IgniteClient igniteInstance() {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("192.168.31.237:10800").setPartitionAwarenessEnabled(true);
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
        storageCfg.setDefaultWarmUpConfiguration(new LoadAllWarmUpConfiguration());
        cfg.setTransactionConfiguration(new ClientTransactionConfiguration().setDefaultTxTimeout(10000).setDefaultTxConcurrency(TransactionConcurrency.OPTIMISTIC).setDefaultTxIsolation(TransactionIsolation.REPEATABLE_READ));
        IgniteClient igniteClient = Ignition.startClient(cfg);
        return igniteClient;
    }
}