package dev.mvv.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenStringBuilder {

    private final List<Token> tokens;

    public TokenStringBuilder() {
        tokens = new ArrayList<>();
    }

    public TokenStringBuilder addWord(Word word) {
        tokens.add(word);
        return this;
    }

    public TokenStringBuilder addWord(String word) {
        return addWord(new Word(word));
    }

    public TokenStringBuilder addWords(String... words) {
        for (String word : words) {
            addWord(word);
        }
        return this;
    }

    public TokenStringBuilder addBracketedWords(BracketedWords words) {
        tokens.add(words);
        return this;
    }

    public TokenStringBuilder addBracketedWords(Bracket bracket, String... words) {
        return addBracketedWords(new BracketedWords(Arrays.stream(words).map(Word::new).toList(), bracket));
    }

    public TokenString build() {
        return new TokenString(tokens);
    }
}
