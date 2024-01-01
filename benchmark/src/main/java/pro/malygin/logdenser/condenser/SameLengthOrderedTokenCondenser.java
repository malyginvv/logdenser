package pro.malygin.logdenser.condenser;

import org.jetbrains.annotations.NotNull;
import pro.malygin.logdenser.distance.EditDistanceCalculator;
import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.token.TokenString;

import java.util.Comparator;
import java.util.List;

public class SameLengthOrderedTokenCondenser extends SameLengthTokenCondenser {

    public SameLengthOrderedTokenCondenser(EditDistanceCalculator editDistanceCalculator, CondensingMatcher condensingMatcher) {
        super(editDistanceCalculator, condensingMatcher);
    }

    public SameLengthOrderedTokenCondenser(EditDistanceCalculator editDistanceCalculator, int maxDistance) {
        super(editDistanceCalculator, maxDistance);
    }

    @Override
    public @NotNull List<@NotNull SameResults> condense(@NotNull List<@NotNull TokenString> tokenStrings) {
        tokenStrings.sort(Comparator.comparing(TokenString::toString));
        return super.condense(tokenStrings);
    }
}
