package dev.mvv.result;

/**
 * Represents static or unchanging part of a larger result.
 *
 * @param part The static string part of the result.
 */
public record StaticPart(String part) implements ResultPart {

    @Override
    public String toString() {
        return part;
    }
}
