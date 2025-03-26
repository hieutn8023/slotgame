package com.company.slot.game.service;

import com.company.slot.game.config.GameConfig;
import com.company.slot.game.entity.SpinResult;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
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

    private final GameConfig gameConfig;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Random random = new Random();

    public SpinResult spin() {
        List<List<Object>> matrix = generateRandomMatrix();
        List<List<List<Integer>>> winningLines = findWinningLines(matrix);
        int totalWin = calculateTotalWin(winningLines);
        return new SpinResult(matrix, winningLines, totalWin);
    }

    private List<List<Object>> generateRandomMatrix() {
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
        return IntStream.range(0, numSpins)
                .mapToObj(i -> CompletableFuture.supplyAsync(this::spin, executor))
                .map(CompletableFuture::join)
                .toList();
    }

    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
