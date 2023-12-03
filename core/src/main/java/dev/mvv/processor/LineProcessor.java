package dev.mvv.processor;

import dev.mvv.token.TokenString;
import dev.mvv.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * Responsible for processing input lines by applying a series of transformations and tokenizing the resulting string.
 */
public class LineProcessor {

    private final UnaryOperator<String> transformer;
    private final Tokenizer tokenizer;

    /**
     * Constructs a LineProcessor.
     *
     * @param transformer A function that transforms the input line of text before tokenization.
     * @param tokenizer   A Tokenizer implementation used to break the transformed string into tokens.
     */
    public LineProcessor(@NotNull UnaryOperator<String> transformer, @NotNull Tokenizer tokenizer) {
        this.transformer = transformer;
        this.tokenizer = tokenizer;
    }

    /**
     * Processes a given input line by applying the transformer and tokenizing the result.
     *
     * @param line The input line of text to be processed.
     * @return Tokens extracted from the transformed input line.
     */
    public @NotNull TokenString process(@NotNull String line) {
        return tokenizer.tokenize(transformer.apply(line));
    }
}
