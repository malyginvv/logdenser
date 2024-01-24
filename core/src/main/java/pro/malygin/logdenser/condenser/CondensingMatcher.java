package pro.malygin.logdenser.condenser;

import org.jetbrains.annotations.NotNull;
import pro.malygin.logdenser.distance.EditDistance;
import pro.malygin.logdenser.token.TokenString;

/**
 * Defines the contract for classes responsible for determining
 * whether two tokenized strings should be condensed based on specific matching criteria.
 */
@FunctionalInterface
public interface CondensingMatcher {

    /**
     * Determines whether two tokenized strings should be condensed based on certain matching criteria.
     *
     * @param left         The first TokenString for comparison.
     * @param right        The second TokenString for comparison.
     * @param editDistance The EditDistance between the strings.
     * @return {@code true} if the {@code left} and {@code right} tokenized strings should be condensed together based
     * on the matching criteria, otherwise {@code false}.
     */
    boolean matches(@NotNull TokenString left, @NotNull TokenString right, @NotNull EditDistance editDistance);
}
