package pro.malygin.logdenser.result;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a full result combined from multiple parts.
 *
 * @param parts A list of elements that make up the result.
 */
public record FullResult(@NotNull List<? extends ResultPart> parts) {

    public String template() {
        var stringBuilder = new StringBuilder();
        int counter = 0;
        for (ResultPart part : parts) {
            if (part.isStatic()) {
                stringBuilder.append(part);
            } else {
                stringBuilder.append('$').append(counter++);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return parts.stream()
                .map(ResultPart::toString)
                .collect(Collectors.joining(" "));
    }
}
