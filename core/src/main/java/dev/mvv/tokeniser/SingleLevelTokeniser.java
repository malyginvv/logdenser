package dev.mvv.tokeniser;

import dev.mvv.token.BracketedWords;
import dev.mvv.token.Token;
import dev.mvv.token.Word;

import java.util.ArrayList;
import java.util.List;

import static dev.mvv.token.Bracket.NONE;
import static dev.mvv.token.Bracket.byClosing;
import static dev.mvv.token.Bracket.byOpening;
import static java.lang.Character.isWhitespace;
import static java.util.Collections.emptyList;

public class SingleLevelTokeniser implements Tokeniser {

    @Override
    public List<Token> tokenise(String s) {
        if (s.isEmpty()) {
            return emptyList();
        }

        char[] charArray = s.toCharArray();
        int from = 0;
        var bracket = NONE;
        var result = new ArrayList<Token>();
        var bracketed = new ArrayList<Word>();
        for (int i = 1; i < charArray.length; i++) {
            char c = charArray[i];
            if (isWhitespace(c)) {
                if (i > from) {
                    var word = new Word(String.valueOf(charArray, from, i - from));
                    if (bracket == NONE) {
                        result.add(word);
                    } else {
                        bracketed.add(word);
                    }
                }
                from = i + 1;
            } else {
                if (bracket == NONE) {
                    var opening = byOpening(c);
                    if (opening != NONE) {
                        bracket = opening;
                        from = i + 1;
                    }
                } else {
                    var closing = byClosing(c);
                    if (closing == bracket) {
                        bracketed.add(new Word(String.valueOf(charArray, from, i - from)));
                        result.add(new BracketedWords(new ArrayList<>(bracketed), bracket));
                        bracketed.clear();
                        bracket = NONE;
                        from = i + 1;
                    }
                }
            }
        }

        if (from < charArray.length) {
            result.addAll(bracketed);
            result.add(new Word(String.valueOf(charArray, from, charArray.length - from)));
        }

        return result;
    }
}
