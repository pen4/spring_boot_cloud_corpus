package com.springboot.tools.config;

import com.springboot.tools.service.inter.HbaseOperationsImpl;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * @author kxd
 * 配置读取
 */
@Configuration
@EnableConfigurationProperties(HBaseProperties.class)
public class HBaseConfig {

    private final HBaseProperties properties;

    public HBaseConfig(HBaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {

        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();

        Map<String, String> config = properties.getConfig();

        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }
        return configuration;
    }


    @Bean
    @ConditionalOnMissingBean(HbaseOperationsImpl.class)
    public HbaseOperationsImpl hbaseTemplate() {
        return new HbaseOperationsImpl(configuration());
    }

}
