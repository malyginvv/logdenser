package pro.malygin.logdenser.condenser;

import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.token.TokenString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Condenses a list of token strings into groups of identical results.
 */
public interface TokenCondenser {

    /**
     * Condenses a list of token strings into groups of identical results.
     *
     * @param tokenStrings A list of token strings.
     * @return A list representing groups of identical token strings of the same length.
     */
    @NotNull List<@NotNull SameResults> condense(@NotNull List<@NotNull TokenString> tokenStrings);
}
