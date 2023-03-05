package dev.mvv.distance;

import dev.mvv.token.BracketedWords;
import dev.mvv.token.TokenString;
import dev.mvv.token.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static dev.mvv.token.Bracket.ROUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LevenshteinDistanceCalculatorTest {

    private LevenshteinDistanceCalculator calculator;

    public static Stream<Arguments> tokenStrings() {
        return Stream.of(
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        0
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        1
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        1
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("amet"), new Word("sit"))),
                        1
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("ipsum"), new Word("sit"), new Word("amet"))),
                        2
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new BracketedWords(List.of(new Word("test"), new Word("one")), ROUND))),
                        1
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new BracketedWords(List.of(new Word("test"), new Word("one")), ROUND))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new BracketedWords(List.of(new Word("test"), new Word("one")), ROUND))),
                        0
                ),
                arguments(
                        new TokenString(List.of()),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new BracketedWords(List.of(new Word("test"), new Word("one")), ROUND))),
                        3
                )
        );
    }

    @BeforeEach
    void setUp() {
        calculator = new LevenshteinDistanceCalculator();
    }

    @MethodSource("tokenStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void shouldCalculateDistance(TokenString first, TokenString second, int expectedDistance) {
        var distance = calculator.distance(first, second);

        assertEquals(expectedDistance, distance);
    }
}