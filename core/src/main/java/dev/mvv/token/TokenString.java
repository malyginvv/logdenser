package dev.mvv.token;

import java.util.List;
import java.util.stream.Collectors;

public record TokenString(List<Token> tokens) {

    public String content() {
        return tokens.stream()
                .map(Token::content)
                .collect(Collectors.joining(" "));
    }
}
