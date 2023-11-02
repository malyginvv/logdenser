package dev.mvv.token;

import org.jetbrains.annotations.NotNull;

/**
 * Tokens are the basic units of text that are used in many natural language
 * processing tasks, such as machine translation, text analysis, and speech recognition.
 * Tokens are typically words, phrases, or other meaningful units.
 */
public interface Token {

    /**
     * Returns a string representation of token.
     * Implementations might return the same object for every call.
     *
     * @return string representation of token
     */
    @NotNull String content();
}
