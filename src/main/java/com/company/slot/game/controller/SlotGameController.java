package com.company.slot.game.controller;

import com.company.slot.game.SlotGameApplication;
import com.company.slot.game.config.GameConfig;
import com.company.slot.game.entity.GameConfigResponse;
import com.company.slot.game.entity.SpinResult;
import com.company.slot.game.service.SlotGameService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SlotGameController {
    private static final Logger log = LoggerFactory.getLogger(SlotGameController.class);
    private final SlotGameService slotGameService;
    private final GameConfig gameConfig;

    @PostMapping("/spin")
    public SpinResult spin() {
        return slotGameService.spin();
    }

    @GetMapping("/config")
    public GameConfigResponse getConfig() {
        return gameConfig.getConfig();
    }

    @PostMapping("/benchmark")
    public String benchmark(@RequestParam int spins) throws ExecutionException, InterruptedException {
        long start = System.nanoTime();
        List<SpinResult> results = slotGameService.executeMultipleSpins(spins);
        long end = System.nanoTime();
        long duration = (end - start) / 1_000_000; // Convert to milliseconds
        return "Executed " + spins + " spins in " + duration + " ms (" + (spins * 1000.0 / duration) + " spins/sec)";
    }
}
