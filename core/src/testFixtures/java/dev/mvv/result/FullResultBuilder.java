package dev.mvv.result;

import java.util.ArrayList;
import java.util.List;

public class FullResultBuilder {

    private final List<ResultPart> parts;

    public FullResultBuilder() {
        this.parts = new ArrayList<>();
    }

    public FullResultBuilder addStatic(String string) {
        parts.add(new StaticPart(string));
        return this;
    }

    public FullResultBuilder addStatics(String... strings) {
        for (String string : strings) {
            parts.add(new StaticPart(string));
        }
        return this;
    }

    public FullResultBuilder addOptions(String... strings) {
        parts.add(new MultipleOptions(List.of(strings)));
        return this;
    }

    public FullResult build() {
        return new FullResult(parts);
    }
}
