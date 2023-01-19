package com.springboot.tools.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author kxd
 * @date 2018/12/18 22:07
 * description: 七牛云相关配置文件
 */

@Data
@ConfigurationProperties(prefix = "qiniu")
@PropertySource("classpath:qiniucloud.properties")
@Component
public class QiNiuPropertiesConfig implements ApplicationContextAware {
    private String testAccessKey;
    private String testSecretKey;
    private String upHttpUrlTest;
    private String ioVipHttpTest;
    private String rsHttpTest;
    private String rsfHttpTest;
    private String apiHttpTest;

    private String proAccessKey;
    private String proSecretKey;
    private String upHttpUrlPro;
    private String ioVipHttpPro;
    private String rsHttpPro;
    private String rsfHttpPro;
    private String apiHttpPro;


    private String downLoadSavePath;
    private ApplicationContext context = null;

    /// 获取当前环境
    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
