package dev.mvv.tokeniser;

import dev.mvv.token.BracketedWords;
import dev.mvv.token.Token;
import dev.mvv.token.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static dev.mvv.token.Bracket.ROUND;
import static dev.mvv.token.Bracket.SQUARE;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SingleLevelTokeniserTest {

    private SingleLevelTokeniser tokeniser;

    public static Stream<Arguments> tokenisedStrings() {
        return Stream.of(
                arguments(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing",
                        List.of(new Word("Lorem"), new Word("ipsum"), new Word("dolor"), new Word("sit"),
                                new Word("amet,"), new Word("consectetur"), new Word("adipiscing"))
                ),
                arguments(
                        "Lorem [ipsum dolor sit] amet, (consectetur adipiscing)",
                        List.of(new Word("Lorem"),
                                new BracketedWords(List.of(new Word("ipsum"), new Word("dolor"), new Word("sit")), SQUARE),
                                new Word("amet,"),
                                new BracketedWords(List.of(new Word("consectetur"), new Word("adipiscing")), ROUND))
                )
        );
    }

    @BeforeEach
    void setUp() {
        tokeniser = new SingleLevelTokeniser();
    }

    @MethodSource("tokenisedStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void shouldTokeniseNonEmptyStrings(String input, List<Token> expected) {
        var tokens = tokeniser.tokenise(input);

        assertIterableEquals(expected, tokens);
    }
}