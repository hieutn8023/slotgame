package com.company.slot.game.config;

import com.company.slot.game.entity.GameConfigResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class GameConfig {
    public static final int ROWS = 3;
    public static final int COLUMNS = 5;
    public static final int TOTAL_LINES = 20;
    public static final String WILD = "Wild";

    private final List<int[][]> winningLines = List.of(
            new int[][]{{0, 0}, {0, 1}, {0, 2}}, // Example horizontal line
            new int[][]{{1, 0}, {1, 1}, {1, 2}},
            new int[][]{{2, 0}, {2, 1}, {2, 2}},
            new int[][]{{0, 0}, {1, 1}, {2, 2}}, // Diagonal
            new int[][]{{2, 0}, {1, 1}, {0, 2}}  // Diagonal
            // Add 15 more winning lines...
    );

    public GameConfigResponse getConfig() {
        return new GameConfigResponse(ROWS, COLUMNS, WILD, winningLines.size(), winningLines);
    }
}
