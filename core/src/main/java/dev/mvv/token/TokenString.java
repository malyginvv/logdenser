package dev.mvv.token;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A sequence of tokens.
 *
 * @param tokens list of tokens
 */
public record TokenString(@NotNull List<@NotNull Token> tokens) {

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    public @NotNull Token firstToken() {
        return tokens.get(0);
    }

    public @NotNull Token token(int index) {
        return tokens.get(index);
    }

    /**
     * Returns number of tokens in this sequence.
     *
     * @return number of tokens
     */
    public int size() {
        return tokens.size();
    }

    @Override
    public String toString() {
        return tokens.stream()
                .map(Token::content)
                .collect(Collectors.joining(" "));
    }
}
