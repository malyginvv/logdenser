package dev.mvv.result;

/**
 * Represents a grouping of results that are considered the same or identical.
 *
 * @param fullResult The reference representing the identical results.
 * @param count      The count of identical results.
 * @since 1.0
 */
public record SameResults(FullResult fullResult, int count) {

    @Override
    public String toString() {
        return fullResult + " {" + count + "}";
    }
}
