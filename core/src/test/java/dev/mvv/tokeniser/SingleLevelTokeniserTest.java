package dev.mvv.tokeniser;

import dev.mvv.token.TokenString;
import dev.mvv.token.TokenStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static dev.mvv.token.Bracket.ROUND;
import static dev.mvv.token.Bracket.SQUARE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SingleLevelTokeniserTest {

    private SingleLevelTokeniser tokeniser;

    public static Stream<Arguments> tokenisedStrings() {
        return Stream.of(
                arguments(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing",
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "dolor", "sit", "amet,",
                                "consectetur", "adipiscing").build()
                ),
                arguments(
                        "Lorem [ipsum dolor sit] amet, (consectetur adipiscing)",
                        new TokenStringBuilder().addWord("Lorem").addBracketedWords(SQUARE, "ipsum", "dolor", "sit")
                                .addWord("amet,").addBracketedWords(ROUND, "consectetur", "adipiscing").build()
                ),
                arguments(
                        "Lorem[ipsum dolor sit] amet, 100(ms)",
                        new TokenStringBuilder().addWord("Lorem").addBracketedWords(SQUARE, "ipsum", "dolor", "sit")
                                .addWord("amet,").addWord("100").addBracketedWords(ROUND, "ms").build()
                ),
                arguments(
                        "Lorem[ipsum [dolor] sit] amet, (consectetur (adipiscing))",
                        new TokenStringBuilder().addWord("Lorem").addBracketedWords(SQUARE, "ipsum", "[dolor")
                                .addWord("sit]").addWord("amet,").addBracketedWords(ROUND, "consectetur", "(adipiscing")
                                .addWord(")").build()
                )
        );
    }

    @BeforeEach
    void setUp() {
        tokeniser = new SingleLevelTokeniser();
    }

    @MethodSource("tokenisedStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void shouldTokeniseNonEmptyStrings(String input, TokenString expected) {
        var tokens = tokeniser.tokenise(input);

        assertEquals(expected, tokens);
    }
}