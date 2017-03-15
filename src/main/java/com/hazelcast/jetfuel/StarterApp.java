package com.hazelcast.jetfuel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class StarterApp {

    @Component
    public static class Launcher implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StarterApp.class, args);
    }
}
