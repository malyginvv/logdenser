package dev.mvv.distance;

import dev.mvv.token.TokenString;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.min;
import static java.util.Collections.emptyList;

public class LevenshteinDistanceCalculator implements EditDistanceCalculator {
    @Override
    public @NotNull EditDistance distance(@NotNull TokenString first, @NotNull TokenString second) {
        var firstLength = first.tokens().size();
        var secondLength = second.tokens().size();
        if (first.tokens().isEmpty()) {
            return new EditDistance(secondLength, emptyList());
        }
        if (second.tokens().isEmpty()) {
            return new EditDistance(firstLength, emptyList());
        }
        if (first.equals(second)) {
            return new EditDistance(0, emptyList());
        }

        var rows = firstLength + 1;
        var cols = secondLength + 1;
        int[][] distances = new int[firstLength + 1][secondLength + 1];
        for (var i = 1; i < cols; i++) {
            distances[0][i] = i;
        }
        for (var i = 1; i < rows; i++) {
            distances[i][0] = i;
        }
        for (var r = 1; r < rows; r++) {
            for (var c = 1; c < cols; c++) {
                int prefixDistance = first.tokens().get(r - 1).equals(second.tokens().get(c - 1)) ? 0 : 1;
                int distance = min(min(distances[r - 1][c], distances[r][c - 1]), distances[r - 1][c - 1]);
                distances[r][c] = distance + prefixDistance;
            }
        }

        return new EditDistance(distances[firstLength][secondLength], emptyList());
    }
}
