package com.company.slot.game.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SpinResult {
    private final List<List<Object>> spinResult;
    private final List<List<List<Integer>>> winningLines;
    private final int totalWin;
}
