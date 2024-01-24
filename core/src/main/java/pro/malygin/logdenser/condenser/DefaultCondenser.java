package pro.malygin.logdenser.condenser;

import org.jetbrains.annotations.NotNull;
import pro.malygin.logdenser.distance.HammingDistanceCalculator;
import pro.malygin.logdenser.processor.LineProcessor;
import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.token.TokenString;
import pro.malygin.logdenser.tokenizer.SingleLevelTokenizer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.groupingBy;

/**
 * A simple utility class for condensing strings.
 * It uses default implementations for its components where it's possible.
 */
public class DefaultCondenser {

    private final Predicate<String> filter;
    private final LineProcessor lineProcessor;
    private final TokenCondenser tokenCondenser;

    public DefaultCondenser(@NotNull Predicate<String> filter,
                            @NotNull LineProcessor lineProcessor,
                            @NotNull TokenCondenser tokenCondenser) {
        this.filter = filter;
        this.lineProcessor = lineProcessor;
        this.tokenCondenser = tokenCondenser;
    }

    public DefaultCondenser(@NotNull Predicate<String> filter,
                            @NotNull UnaryOperator<String> transformer,
                            @NotNull CondensingMatcher condensingMatcher) {
        this(
                filter,
                new LineProcessor(
                        transformer,
                        new SingleLevelTokenizer(Map.of('[', ']', '(', ')', '{', '}'))
                ),
                new SameLengthTokenCondenser(
                        new HammingDistanceCalculator(),
                        condensingMatcher
                )
        );
    }

    public DefaultCondenser(@NotNull UnaryOperator<String> transformer,
                            @NotNull CondensingMatcher condensingMatcher) {
        this(s -> true, transformer, condensingMatcher);
    }

    public DefaultCondenser(@NotNull CondensingMatcher condensingMatcher) {
        this(identity(), condensingMatcher);
    }

    public @NotNull List<SameResults> condense(@NotNull Collection<String> input) {
        var bySize = input.stream()
                .filter(filter)
                .map(lineProcessor::process)
                .filter(tokenString -> !tokenString.tokens().isEmpty())
                .collect(groupingBy(TokenString::size));

        return bySize.entrySet().stream()
                .flatMap((entry) -> tokenCondenser.condense(entry.getValue()).stream())
                .toList();
    }
}
