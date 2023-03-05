package dev.mvv.token;

import java.util.Collection;
import java.util.Objects;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.joining;

public class BracketedWords implements Token {

    public final Collection<Word> words;
    public final Bracket bracket;
    public final String content;

    public BracketedWords(Collection<Word> words, Bracket bracket) {
        this.words = unmodifiableCollection(words);
        this.bracket = bracket;
        this.content = bracket.opening + words.stream().map(Word::word).collect(joining(" ")) + bracket.closing;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BracketedWords that = (BracketedWords) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "BracketedWords{" +
                "content='" + content + '\'' +
                '}';
    }
}
