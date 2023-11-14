package dev.mvv.distance;

import dev.mvv.token.TokenString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dev.mvv.distance.EditType.DELETION;
import static dev.mvv.distance.EditType.INSERTION;
import static dev.mvv.distance.EditType.SUBSTITUTION;
import static java.lang.Math.min;

/**
 * Calculates the Hamming distance between two sequences.
 * The Hamming distance is a measure of dissimilarity between two sequences of equal length,
 * and it quantifies the number of positions at which the corresponding elements of the sequences are different.
 * However, this implementation supports distance calculation between two sequences of any length.
 * <p>
 * Examples:
 * <ul>
 *     <li>Distance between {@code "abcd"} and {@code "adcb"} is 2. Edits are: {@code [1S, 3S]}</li>
 *     <li>Distance between {@code "abcd"} and {@code "abc"} is 1. Edits are: {@code [3D]}</li>
 *     <li>Distance between {@code "abc"} and {@code "abcd"} is 1. Edits are: {@code [3I]}</li>
 *     <li>Distance between {@code "abd"} and {@code "abcd"} is 2. Edits are: {@code [2S, 3I]}</li>
 *     <li>Distance between {@code ""} and {@code "abcd"} is 4. Edits are: {@code [0I, 1I, 2I, 3I]}</li>
 * </ul>
 */
public class HammingDistanceCalculator implements EditDistanceCalculator {

    @Override
    public @NotNull EditDistance distance(@NotNull TokenString first, @NotNull TokenString second) {
        var firstSize = first.size();
        var secondSize = second.size();
        var minSize = min(firstSize, secondSize);
        var edits = new ArrayList<Edit>();
        for (int i = 0; i < minSize; i++) {
            if (!first.token(i).content().equals(second.token(i).content())) {
                edits.add(new Edit(i, SUBSTITUTION));
            }
        }
        for (int i = minSize; i < firstSize; i++) {
            edits.add(new Edit(i, DELETION));
        }
        for (int i = minSize; i < secondSize; i++) {
            edits.add(new Edit(i, INSERTION));
        }
        return new EditDistance(edits.size(), edits);
    }
}
