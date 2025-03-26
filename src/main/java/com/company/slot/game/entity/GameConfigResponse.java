package com.company.slot.game.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameConfigResponse {
    private final int rows;
    private final int columns;
    private final String wildSymbol;
    private final int totalWinningLines;
    private final List<int[][]> winningLines;
}
