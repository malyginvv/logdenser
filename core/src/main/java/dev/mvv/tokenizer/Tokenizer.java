package dev.mvv.tokenizer;

import dev.mvv.token.TokenString;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for tokenizing strings.
 * <p>
 * A tokenizer is an object that splits a string into a sequence of tokens.
 * Tokens are the basic units of text that are used in many natural language
 * processing tasks, such as machine translation, text analysis, and speech recognition.
 * <p>
 * The specific way that a tokenizer splits a string into tokens is implementation-dependent.
 * However, most tokenizers will split strings on whitespace characters, such as spaces,
 * tabs, and newlines. Some tokenizers may also split strings on punctuation marks,
 * such as commas, periods, and semicolons.
 */
public interface Tokenizer {

    /**
     * Tokenizes the given string.
     *
     * @param s the string to tokenize
     * @return the tokenized string
     */
    @NotNull TokenString tokenize(@NotNull String s);
}
