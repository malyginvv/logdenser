package dev.mvv.result;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a grouping of results that are considered the same or identical.
 *
 * @param fullResult The reference representing the identical results.
 * @param count      The count of identical results.
 */
public record SameResults(@NotNull FullResult fullResult, int count) {

    @Override
    public String toString() {
        return fullResult + " {" + count + "}";
    }
}
