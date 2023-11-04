package dev.mvv.token;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.joining;

/**
 * A token that represents a word sequence enclosed between opening and closing characters.
 * Usually opening and closing characters are pairs of brackets or parenthesis.
 * <p>
 * For example, the following string {@code "[some text]"}
 * could be represented as {@code EnclosedWords{words=["some", "text"], opening='[', closing=']'}}.
 * <p>
 * This class is thread-safe.
 */
public class EnclosedWords implements Token {

    public final Collection<Word> words;
    public final char opening;
    public final char closing;
    public final String content;

    /**
     * Constructs a token containing words from the specified collection,
     * using specified opening and closing characters.
     *
     * @param words   word sequence
     * @param opening opening character
     * @param closing closing character
     */
    public EnclosedWords(Collection<Word> words, char opening, char closing) {
        this.words = unmodifiableCollection(words);
        this.opening = opening;
        this.closing = closing;
        this.content = opening + words.stream().map(Word::content).collect(joining(" ")) + closing;
    }

    @Override
    public @NotNull String content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnclosedWords that = (EnclosedWords) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "EnclosedWords{" +
                "words=" + words +
                ", opening=" + opening +
                ", closing=" + closing +
                '}';
    }
}
