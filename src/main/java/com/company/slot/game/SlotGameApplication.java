package com.company.slot.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class SlotGameApplication {
    private static final Logger log = LoggerFactory.getLogger(SlotGameApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SlotGameApplication.class, args);
        log.info("Slot Game Application started successfully");
    }
}
