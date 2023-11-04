package dev.mvv.result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a full result combined from multiple parts.
 *
 * @param parts A list of elements that make up the result.
 */
public record FullResult(List<? extends ResultPart> parts) {

    @Override
    public String toString() {
        return parts.stream()
                .map(ResultPart::toString)
                .collect(Collectors.joining(" "));
    }
}
