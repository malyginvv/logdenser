package pro.malygin.logdenser.result;

import org.jetbrains.annotations.NotNull;

/**
 * Represents static or unchanging part of a larger result.
 *
 * @param part The static string part of the result.
 */
public record StaticPart(@NotNull String part) implements ResultPart {
    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public String toString() {
        return part;
    }
}
