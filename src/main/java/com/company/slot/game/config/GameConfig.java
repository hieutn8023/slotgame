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
    public static final String WILD = "Wild";

    private final List<int[][]> winningLines = List.of(
            // 20 Winning Lines (Horizontal, Diagonal, Zig-Zag)
            new int[][]{{0, 0}, {0, 1}, {0, 2}},
            new int[][]{{1, 0}, {1, 1}, {1, 2}},
            new int[][]{{2, 0}, {2, 1}, {2, 2}},
            new int[][]{{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}},
            new int[][]{{1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}},
            new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}},
            new int[][]{{0, 0}, {1, 1}, {2, 2}},
            new int[][]{{2, 0}, {1, 1}, {0, 2}},
            new int[][]{{0, 4}, {1, 3}, {2, 2}},
            new int[][]{{2, 4}, {1, 3}, {0, 2}},
            new int[][]{{0, 0}, {1, 1}, {0, 2}},
            new int[][]{{1, 0}, {2, 1}, {1, 2}},
            new int[][]{{2, 0}, {1, 1}, {2, 2}},
            new int[][]{{0, 2}, {1, 1}, {2, 0}},
            new int[][]{{2, 2}, {1, 1}, {0, 0}},
            new int[][]{{0, 0}, {2, 1}, {0, 2}},
            new int[][]{{2, 0}, {0, 1}, {2, 2}},
            new int[][]{{1, 0}, {0, 1}, {1, 2}},
            new int[][]{{1, 0}, {2, 1}, {1, 2}}
    );

    public static final int TOTAL_LINES = 20;

    public GameConfigResponse getConfig() {
        return new GameConfigResponse(ROWS, COLUMNS, WILD, TOTAL_LINES, winningLines);
    }
}
