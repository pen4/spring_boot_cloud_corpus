package com.springboot.task;

import com.springboot.ws.VehiclePosWs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;


/**
 * @author: kangxd
 * @date: 2022/10/26
 * @Description: 此类用于scheduling
 */

@Component
@EnableScheduling
@Slf4j
@ConditionalOnProperty(prefix = "scheduling", name = "enable", havingValue = "true")
public class VehiclePosWsSendMessageTask {
    //数据库操作
    //@Autowired
    //private VehiclePositionCacheService vehiclePositionCacheService;

    /**
     * 推送任务
     */
    @Scheduled(fixedRate = 500)
    public void doTask() {

        Set<VehiclePosWs> vehiclePosWs = VehiclePosWs.getWebSocketSet();
        if (!CollectionUtils.isEmpty(vehiclePosWs)) {
            log.info("开始推送给前端数据");
            vehiclePosWs.forEach(v -> {
                //调用sendMessage发送数据
             /*   VehiclePositionCacheVO cacheVo = vehiclePositionCacheService.getPosCacheVO();
                v.sendMessage(JSON.toJSONString(cacheVo));*/
            });
            log.info("推送给前端数据结束");
        }
    }

}
