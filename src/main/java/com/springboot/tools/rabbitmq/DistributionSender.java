package com.springboot.tools.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kxd
 * @date 2019/3/7 16:30
 * description:
 */
@Component
public class DistributionSender {

    @Autowired(required = false)
    private AmqpTemplate amqpTemplate;

   public void send(int i){
       String message="this is a task,and the complexity is "+i+","+ StringUtils.repeat(".",i);
       this.amqpTemplate.convertAndSend("distribu",message);
   }

}
