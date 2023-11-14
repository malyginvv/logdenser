package dev.mvv.distance;

import dev.mvv.token.EnclosedWords;
import dev.mvv.token.TokenString;
import dev.mvv.token.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
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
                        new EditDistance(0, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        new EditDistance(1, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new EditDistance(1, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("amet"), new Word("sit"))),
                        new EditDistance(1, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("ipsum"), new Word("sit"), new Word("amet"))),
                        new EditDistance(2, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new Word("sit"))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new EnclosedWords(List.of(new Word("test"), new Word("one")), '(', ')'))),
                        new EditDistance(1, emptyList())
                ),
                arguments(
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new EnclosedWords(List.of(new Word("test"), new Word("one")), '(', ')'))),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new EnclosedWords(List.of(new Word("test"), new Word("one")), '(', ')'))),
                        new EditDistance(0, emptyList())
                ),
                arguments(
                        new TokenString(List.of()),
                        new TokenString(List.of(new Word("Lorem"), new Word("ipsum"), new EnclosedWords(List.of(new Word("test"), new Word("one")), '(', ')'))),
                        new EditDistance(3, emptyList())
                )
        );
    }

    @BeforeEach
    void setUp() {
        calculator = new LevenshteinDistanceCalculator();
    }

    @MethodSource("tokenStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void should_calculate_distance(TokenString first, TokenString second, EditDistance expectedDistance) {
        var distance = calculator.distance(first, second);

        assertEquals(expectedDistance, distance);
    }
}