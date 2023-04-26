package com.state.machine;

import com.state.machine.enums.Events;
import com.state.machine.enums.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

/**
 * @author: User
 * @date: 2023/4/20
 * @Description:此类用于xxx
 */
@SpringBootApplication
public class App  {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private StateMachine<States, Events> stateMachine;

 /*   @Override
    public void run(String... args) throws Exception {
        stateMachine.sendEvent(Events.E1);
        stateMachine.sendEvent(Events.E2);
    }*/
}