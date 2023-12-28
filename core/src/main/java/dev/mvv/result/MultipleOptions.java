package dev.mvv.result;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * It represents a set of multiple options or variations as a part of a result.
 *
 * @param options A list of string options.
 */
public record MultipleOptions(@NotNull List<@NotNull String> options) implements ResultPart {
    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String toString() {
        return "<" + String.join("|", options) + ">";
    }
}
