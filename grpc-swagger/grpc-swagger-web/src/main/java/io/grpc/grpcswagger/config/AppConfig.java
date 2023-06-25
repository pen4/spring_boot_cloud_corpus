package io.grpc.grpcswagger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    
    private static AppConfigValues appConfigValues = new AppConfigValues();
   
    @Autowired
    public void setAppConfigValues(AppConfigValues values) {
        appConfigValues = values;
    }
    
    public static boolean enableListService() {
        
        return appConfigValues.isEnableListService();
    }
    
    public static int serviceExpiredSeconds() {
        return appConfigValues.getServiceExpiredSeconds();
    }
}
