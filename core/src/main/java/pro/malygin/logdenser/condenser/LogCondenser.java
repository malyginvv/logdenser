package pro.malygin.logdenser.condenser;

import org.jetbrains.annotations.NotNull;
import pro.malygin.logdenser.input.InputProcessor;
import pro.malygin.logdenser.processor.LineProcessor;
import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.token.TokenString;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;

/**
 * A utility for condensing log information represented by a generic type T.
 * It combines various processing components to filter, transform, and group log data for easier analysis and understanding.
 *
 * @param <T> The generic type representing the log information or input data.
 */
public class LogCondenser<T> {

    private final InputProcessor<T> inputProcessor;
    private final Predicate<String> filter;
    private final LineProcessor lineProcessor;
    private final TokenCondenser tokenCondenser;

    /**
     * Constructs a LogCondenser with the necessary processing components.
     *
     * @param inputProcessor The InputProcessor responsible for processing the input log data of type T.
     * @param filter         A Predicate used for filtering log messages based on specified conditions.
     * @param lineProcessor  The LineProcessor used to process individual log lines for transformation.
     * @param tokenCondenser The TokenCondenser used to condense tokenized log data.
     */
    public LogCondenser(@NotNull InputProcessor<T> inputProcessor,
                        @NotNull Predicate<String> filter,
                        @NotNull LineProcessor lineProcessor,
                        @NotNull TokenCondenser tokenCondenser) {
        this.inputProcessor = inputProcessor;
        this.filter = filter;
        this.lineProcessor = lineProcessor;
        this.tokenCondenser = tokenCondenser;
    }

    public @NotNull List<SameResults> condense(@NotNull T input) {
        var bySize = inputProcessor.process(input)
                .filter(filter)
                .map(lineProcessor::process)
                .filter(tokenString -> !tokenString.tokens().isEmpty())
                .collect(groupingBy(TokenString::size));

        return bySize.entrySet().stream()
                .flatMap((entry) -> tokenCondenser.condense(entry.getValue()).stream())
                .toList();
    }
}
