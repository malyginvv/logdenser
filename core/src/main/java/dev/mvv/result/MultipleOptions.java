package dev.mvv.result;

import java.util.List;

/**
 * It represents a set of multiple options or variations as a part of a result.
 *
 * @param options A list of string options.
 */
public record MultipleOptions(List<String> options) implements ResultPart {
    @Override
    public String toString() {
        return "<" + String.join("|", options) + ">";
    }
}
