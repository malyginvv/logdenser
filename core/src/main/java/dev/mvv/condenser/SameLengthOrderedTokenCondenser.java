package dev.mvv.condenser;

import dev.mvv.distance.EditDistance;
import dev.mvv.distance.EditDistanceCalculator;
import dev.mvv.result.SameResults;
import dev.mvv.token.TokenString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SameLengthOrderedTokenCondenser extends SameLengthTokenCondenser {

    public SameLengthOrderedTokenCondenser(EditDistanceCalculator editDistanceCalculator, BiPredicate<TokenString, TokenString> tokenStringMatcher, Predicate<EditDistance> editDistanceMatcher) {
        super(editDistanceCalculator, tokenStringMatcher, editDistanceMatcher);
    }

    public SameLengthOrderedTokenCondenser(EditDistanceCalculator editDistanceCalculator, BiPredicate<TokenString, TokenString> tokenStringMatcher, int maxDistance) {
        super(editDistanceCalculator, tokenStringMatcher, maxDistance);
    }

    @Override
    public @NotNull List<@NotNull SameResults> condense(@NotNull List<@NotNull TokenString> tokenStrings) {
        tokenStrings.sort(Comparator.comparing(TokenString::toString));
        return super.condense(tokenStrings);
    }
}
