package pro.malygin.logdenser.condenser;

import org.jetbrains.annotations.NotNull;
import pro.malygin.logdenser.distance.Edit;
import pro.malygin.logdenser.distance.EditDistance;
import pro.malygin.logdenser.distance.EditDistanceCalculator;
import pro.malygin.logdenser.result.FullResult;
import pro.malygin.logdenser.result.MultipleOptions;
import pro.malygin.logdenser.result.ResultPart;
import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.result.StaticPart;
import pro.malygin.logdenser.token.Token;
import pro.malygin.logdenser.token.TokenString;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.List;

import static java.lang.System.Logger.Level.DEBUG;
import static java.util.Collections.emptyList;
import static pro.malygin.logdenser.distance.EditType.SUBSTITUTION;

/**
 * Condenses a list of token strings with the same length into groups of identical results.
 * Time complexity of this implementation heavily depends on input variance and matching criteria.
 * In worst case, when almost every input token string is unique, time complexity reaches O(n<sup>2</sup>).
 */
public class SameLengthTokenCondenser implements TokenCondenser {
    private static final Logger LOGGER = System.getLogger(SameLengthTokenCondenser.class.getName());

    private final EditDistanceCalculator editDistanceCalculator;
    private final CondensingMatcher condensingMatcher;

    /**
     * Creates a condenser with explicit condensing matcher.
     *
     * @param editDistanceCalculator Edit distance calculator.
     * @param condensingMatcher      Condensing matcher.
     */
    public SameLengthTokenCondenser(@NotNull EditDistanceCalculator editDistanceCalculator,
                                    @NotNull CondensingMatcher condensingMatcher) {
        this.editDistanceCalculator = editDistanceCalculator;
        this.condensingMatcher = condensingMatcher;
    }

    /**
     * Creates a condenser with distance matcher.
     *
     * @param editDistanceCalculator Edit distance calculator.
     * @param maxDistance            Maximum edit distance for treating two token strings as similar.
     */
    public SameLengthTokenCondenser(@NotNull EditDistanceCalculator editDistanceCalculator,
                                    int maxDistance) {
        this(editDistanceCalculator, ((left, right, editDistance) -> editDistance.distance() <= maxDistance));
    }

    /**
     * Condenses a list of token strings with the same length into groups of identical results.
     *
     * @param tokenStrings A list of token strings.
     * @return A list representing groups of identical token strings of the same length.
     */
    @Override
    public @NotNull List<@NotNull SameResults> condense(@NotNull List<@NotNull TokenString> tokenStrings) {
        if (tokenStrings.isEmpty()) {
            return emptyList();
        }
        if (tokenStrings.size() == 1) {
            return List.of(fromSingle(tokenStrings.get(0)));
        }

        var tokenLength = tokenStrings.get(0).size();
        List<SameResults> results = new ArrayList<>();
        List<TokenString> input = tokenStrings;
        int iteration = 1;
        while (!input.isEmpty()) {
            LOGGER.log(DEBUG, "Condensing token strings, iteration " + iteration + ", input size " + input.size());
            List<TokenString> similar = new ArrayList<>();
            List<TokenString> different = new ArrayList<>();
            BitSet substitutionIndices = new BitSet(tokenLength);

            // partition "input" list into two:
            // "similar" contains all token strings that have matching edit distance between them and the first token string
            // "different" contains all others
            var first = input.get(0);
            checkLength(first, tokenLength);
            similar.add(first);

            for (int i = 1; i < input.size(); i++) {
                var tokenString = input.get(i);
                checkLength(tokenString, tokenLength);
                var editDistance = editDistanceCalculator.distance(first, tokenString);
                if (condensingMatcher.matches(first, tokenString, editDistance)) {
                    similar.add(tokenString);
                    fillSubstitutionIndices(editDistance, substitutionIndices);
                } else {
                    different.add(tokenString);
                }
            } // now "similar" contains at least one element and "different" contains everything else; it might be empty

            LOGGER.log(DEBUG, "Found " + similar.size() + " similar token string at iteration " + iteration);
            results.add(fromList(similar, substitutionIndices));
            input = new ArrayList<>(different);
            iteration++;
        }
        return results;
    }

    private void checkLength(TokenString tokenString, int expectedLength) {
        if (expectedLength != tokenString.size()) {
            throw new IllegalArgumentException("Incorrect number of tokens. " +
                    "Expected " + expectedLength + ", found: " + tokenString.size() + " in {" + tokenString + "}");
        }
    }

    private void fillSubstitutionIndices(EditDistance editDistance, BitSet substitutionIndices) {
        for (Edit edit : editDistance.edits()) {
            if (edit.editType() == SUBSTITUTION) {
                substitutionIndices.set(edit.index());
            }
        }
    }

    private SameResults fromSingle(TokenString tokenString) {
        List<StaticPart> staticParts = new ArrayList<>(tokenString.size());
        for (Token token : tokenString.tokens()) {
            staticParts.add(new StaticPart(token.content()));
        }
        return new SameResults(new FullResult(staticParts), 1);
    }

    private SameResults fromList(List<TokenString> tokenStrings, BitSet substitutionIndices) {
        var staticSource = tokenStrings.get(0);
        var resultSize = staticSource.size();
        var resultParts = new ArrayList<ResultPart>(resultSize);
        for (int i = 0; i < resultSize; i++) {
            ResultPart resultPart;
            if (substitutionIndices.get(i)) {
                var options = new LinkedHashSet<String>();
                for (TokenString tokenString : tokenStrings) {
                    var token = tokenString.token(i);
                    options.add(token.content());
                }
                resultPart = new MultipleOptions(new ArrayList<>(options));
            } else {
                resultPart = new StaticPart(staticSource.token(i).content());
            }
            resultParts.add(resultPart);
        }
        return new SameResults(new FullResult(resultParts), tokenStrings.size());
    }
}
