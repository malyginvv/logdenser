package dev.mvv.distance;

import dev.mvv.token.TokenString;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a contract for classes that calculate the edit distance between two sequences of tokens.
 *
 * @see TokenString
 * @see EditDistance
 */
public interface EditDistanceCalculator {

    /**
     * Calculates the edit distance between two sequences of tokens.
     *
     * @param first  The first sequence to be compared.
     * @param second The second sequence to be compared.
     * @return An edit distance between {@code first} and {@code second}.
     */
    @NotNull EditDistance distance(@NotNull TokenString first, @NotNull TokenString second);
}
