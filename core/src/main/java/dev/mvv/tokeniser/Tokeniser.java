package dev.mvv.tokeniser;

import dev.mvv.token.Token;

import java.util.List;

public interface Tokeniser {
    List<Token> tokenise(String s);
}
