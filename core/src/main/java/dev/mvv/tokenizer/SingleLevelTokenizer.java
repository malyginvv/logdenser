package dev.mvv.tokenizer;

import dev.mvv.token.EnclosedWords;
import dev.mvv.token.Token;
import dev.mvv.token.TokenString;
import dev.mvv.token.Word;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.Character.isWhitespace;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableMap;

public class SingleLevelTokenizer implements Tokenizer {

    private final Map<Character, Character> enclosingMapping;

    public SingleLevelTokenizer(@NotNull Map<Character, Character> enclosingMapping) {
        if (enclosingMapping.isEmpty()) {
            throw new IllegalArgumentException("Enclosing mapping should not be empty.");
        }
        for (Entry<Character, Character> entry : enclosingMapping.entrySet()) {
            if (entry.getKey() == entry.getValue()) {
                throw new IllegalArgumentException("Illegal mapping: " + entry + ". Key and value should not be equal");
            }
        }
        this.enclosingMapping = unmodifiableMap(enclosingMapping);
    }

    @Override
    public @NotNull TokenString tokenize(@NotNull String s) {
        if (s.isEmpty()) {
            return new TokenString(emptyList());
        }

        char[] charArray = s.toCharArray();
        int from = 0;
        int openingsSeen = 0;
        int opening = -1;
        int closing = -1;
        var result = new ArrayList<Token>();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (isWhitespace(c) && openingsSeen == 0) {
                if (i > from) {
                    result.add(new Word(String.valueOf(charArray, from, i - from)));
                }
                from = i + 1;
            } else {
                if (enclosingMapping.containsKey(c)) {
                    if (openingsSeen == 0) {
                        opening = c;
                        // found the first opening, need to save previous characters as a Word
                        if (from < i) {
                            result.add(new Word(String.valueOf(charArray, from, i - from)));
                        }
                        from = i + 1;
                        closing = enclosingMapping.get(c);
                        openingsSeen++;
                    } else if (c == opening) {
                        openingsSeen++;
                    }
                } else if (c == closing) {
                    if (openingsSeen == 1) {
                        // enclosure is complete - tokenize everything from its start to the current char
                        result.add(tokenizeEnclosed(charArray, from, i, (char) opening, (char) closing));
                        from = i + 1;
                    }
                    if (openingsSeen > 0) {
                        openingsSeen--;
                    }
                }
            }
        }

        // reached end of string - tokenize what's left
        if (from < charArray.length) {
            result.addAll(tokenizeWords(charArray, from, charArray.length));
        }

        return new TokenString(result);
    }

    private EnclosedWords tokenizeEnclosed(char[] chars, int from, int to, char opening, char closing) {
        return new EnclosedWords(tokenizeWords(chars, from, to), opening, closing);
    }

    private List<Word> tokenizeWords(char[] chars, int from, int to) {
        var words = new ArrayList<Word>();
        int wordStart = from;
        for (int i = from; i <= to; i++) {
            if (i == to || isWhitespace(chars[i])) {
                if (i > wordStart) {
                    words.add(new Word(String.valueOf(chars, wordStart, i - wordStart)));
                }
                wordStart = i + 1;
            }
        }
        return words;
    }
}
