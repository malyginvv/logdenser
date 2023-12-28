package pro.malygin.logdenser.transformer;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * A transformer that applies a series of individual string transformations sequentially.
 */
public class CompositeTransformer implements UnaryOperator<String> {

    private final List<UnaryOperator<String>> transformers;

    /**
     * Constructs a CompositeTransformer with a list of individual string transformers.
     *
     * @param transformers A list of individual string transformers.
     */
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
