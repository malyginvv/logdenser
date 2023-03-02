package dev.mvv.token;

import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public record BracketedWords(Collection<Word> words, Bracket bracket) implements Token {

    public BracketedWords(Collection<Word> words, Bracket bracket) {
        this.words = unmodifiableCollection(words);
        this.bracket = bracket;
    }
}
