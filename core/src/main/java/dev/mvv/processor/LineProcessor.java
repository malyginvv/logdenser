package dev.mvv.processor;

import dev.mvv.token.TokenString;
import dev.mvv.tokeniser.Tokeniser;

import java.util.function.UnaryOperator;

public class LineProcessor {

    private final UnaryOperator<String> transformer;
    private final Tokeniser tokeniser;

    public LineProcessor(UnaryOperator<String> transformer, Tokeniser tokeniser) {
        this.transformer = transformer;
        this.tokeniser = tokeniser;
    }

    public TokenString process(String line) {
        return tokeniser.tokenise(transformer.apply(line));
    }
}
