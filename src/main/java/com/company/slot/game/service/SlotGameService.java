package com.company.slot.game.service;

import com.company.slot.game.config.GameConfig;
import com.company.slot.game.controller.SlotGameController;
import com.company.slot.game.entity.SpinResult;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SlotGameService {
    private static final Logger log = LoggerFactory.getLogger(SlotGameService.class);
    private final GameConfig gameConfig;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Random random = new Random();

    public SpinResult spin() {
        log.info("🔄 Starting a new spin...");
        long startTime = System.nanoTime();
        List<List<Object>> matrix = generateRandomMatrix();
        log.info("🎰 Generated Matrix: {}", matrix);
        List<List<List<Integer>>> winningLines = findWinningLines(matrix);
        log.info("🏆 Winning Lines Found: {}", winningLines);
        int totalWin = calculateTotalWin(winningLines);
        log.info("💰 Total Win Calculated: {}", totalWin);
        long endTime = System.nanoTime();
        log.info("✅ Spin completed in {} ms", (endTime - startTime) / 1_000_000);
        return new SpinResult(matrix, winningLines, totalWin);
    }

    private List<List<Object>> generateRandomMatrix() {
        log.debug("🔀 Generating random matrix...");
        return IntStream.range(0, GameConfig.ROWS)
                .mapToObj(i -> generateRow())
                .collect(Collectors.toList());
    }

    private List<Object> generateRow() {
        return IntStream.range(0, GameConfig.COLUMNS)
                .mapToObj(j -> generateSymbol())
                .collect(Collectors.toList());
    }

    private Object generateSymbol() {
        int symbol = random.nextInt(10); // Random symbol (0-9)
        return symbol == 9 ? GameConfig.WILD : symbol; // Store numbers as Integer, Wild as String
    }

    private List<List<List<Integer>>> findWinningLines(List<List<Object>> matrix) {
        log.debug("🔍 Checking for winning lines...");
        return gameConfig.getWinningLines().parallelStream()
                .filter(line -> isWinningLine(matrix, line))
                .map(this::convertWinningLineToList)
                .collect(Collectors.toList());
    }

    private boolean isWinningLine(List<List<Object>> matrix, int[][] line) {
        Object firstSymbol = findFirstValidSymbol(matrix, line);
        if (firstSymbol == null) return true; // All symbols are Wild

        return Arrays.stream(line)
                .map(position -> matrix.get(position[0]).get(position[1]))
                .allMatch(symbol -> symbol.equals(firstSymbol) || symbol.equals(GameConfig.WILD));
    }

    private Object findFirstValidSymbol(List<List<Object>> matrix, int[][] line) {
        return Arrays.stream(line)
                .map(position -> matrix.get(position[0]).get(position[1]))
                .filter(symbol -> !symbol.equals(GameConfig.WILD))
                .findFirst()
                .orElse(null);
    }

    private List<List<Integer>> convertWinningLineToList(int[][] line) {
        return Arrays.stream(line)
                .map(coords -> Arrays.asList(coords[0], coords[1]))
                .collect(Collectors.toList());
    }

    private int calculateTotalWin(List<List<List<Integer>>> winningLines) {
        return winningLines.size() * 50; // Example win calculation
    }

    public List<SpinResult> executeMultipleSpins(int numSpins) {
        log.info("🚀 Executing {} spins...", numSpins);
        return IntStream.range(0, numSpins)
                .mapToObj(i -> CompletableFuture.supplyAsync(this::spin, executor))
                .map(CompletableFuture::join)
                .toList();
    }

    @PreDestroy
    public void shutdownExecutor() {
        log.info("🛑 Shutting down executor...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                log.warn("⚠️ Executor forced shutdown due to timeout.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("❌ Executor shutdown interrupted", e);
        }
    }
}
