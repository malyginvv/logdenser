package pro.malygin.logdenser.token;

import org.jetbrains.annotations.NotNull;

/**
 * A simple token representing a sequence of characters.
 *
 * @param word the word
 */
public record Word(String word) implements Token {

    @Override
    public @NotNull String content() {
        return word;
    }
}
