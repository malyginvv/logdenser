package dev.mvv.tokeniser;

import dev.mvv.token.TokenString;

public interface Tokeniser {
    TokenString tokenise(String s);
}
