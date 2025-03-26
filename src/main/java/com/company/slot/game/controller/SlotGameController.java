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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    public Map<String, Object> benchmark(@RequestParam int spins) throws ExecutionException, InterruptedException {
        long startTime = System.nanoTime();

        List<SpinResult> results = slotGameService.executeMultipleSpins(spins);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        double spinsPerSecond = (durationMs > 0) ? (spins * 1000.0 / durationMs) : 0; // Prevent division by zero

        // ✅ Refactored Map construction
        return new HashMap<>(Map.of(
                "totalSpins", spins,
                "averageWin", results.stream().collect(Collectors.averagingInt(SpinResult::getTotalWin)),
                "executionTimeMs", durationMs,
                "spinsPerSecond", spinsPerSecond
        ));
    }
}
