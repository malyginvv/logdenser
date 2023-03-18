package dev.mvv.transformer;

import java.util.List;
import java.util.function.UnaryOperator;

public class CompositeTransformer implements UnaryOperator<String> {

    private final List<UnaryOperator<String>> transformers;

    public CompositeTransformer(List<UnaryOperator<String>> transformers) {
        this.transformers = transformers;
    }

    @Override
    public String apply(String s) {
        for (UnaryOperator<String> transformer : transformers) {
            s = transformer.apply(s);
        }
        return s;
    }
}
