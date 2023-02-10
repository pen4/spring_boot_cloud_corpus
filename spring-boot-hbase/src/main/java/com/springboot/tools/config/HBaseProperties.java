package com.springboot.tools.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author kxd
 * 配置文件
 */
@Data
@ConfigurationProperties(prefix = "hbase")
public class HBaseProperties {
    private Map<String, String> config;
}

