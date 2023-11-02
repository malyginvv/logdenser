package dev.mvv.token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A sequence of tokens.
 *
 * @param tokens list of tokens
 */
public record TokenString(List<Token> tokens) {

    @Override
    public String toString() {
        return tokens.stream()
                .map(Token::content)
                .collect(Collectors.joining(" "));
    }
}
