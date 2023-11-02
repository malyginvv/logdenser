package dev.mvv.tokenizer;

import dev.mvv.token.TokenString;
import dev.mvv.token.TokenStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SingleLevelTokenizerTest {

    private SingleLevelTokenizer tokenizer;

    public static Stream<Arguments> tokenizedStrings() {
        return Stream.of(
                arguments(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing",
                        new TokenStringBuilder()
                                .addWords("Lorem", "ipsum", "dolor", "sit", "amet,", "consectetur", "adipiscing")
                                .build()
                ),
                arguments(
                        "Lorem [ipsum dolor sit] amet, (consectetur adipiscing)",
                        new TokenStringBuilder()
                                .addWord("Lorem").addEnclosedWords('[', ']', "ipsum", "dolor", "sit")
                                .addWord("amet,").addEnclosedWords('(', ')', "consectetur", "adipiscing")
                                .build()
                ),
                arguments(
                        "Lorem[ipsum dolor sit] amet, 100(ms)",
                        new TokenStringBuilder()
                                .addWord("Lorem").addEnclosedWords('[', ']', "ipsum", "dolor", "sit")
                                .addWord("amet,").addWord("100").addEnclosedWords('(', ')', "ms")
                                .build()
                ),
                arguments(
                        "Lorem[ipsum [dolor] sit] amet, (consectetur (adipiscing))",
                        new TokenStringBuilder()
                                .addWord("Lorem").addEnclosedWords('[', ']', "ipsum", "[dolor]", "sit")
                                .addWord("amet,").addEnclosedWords('(', ')', "consectetur", "(adipiscing)")
                                .build()
                ),
                arguments(
                        "[] [[[Lorem]]] ]]]ipsum[[[ dolor ]]]",
                        new TokenStringBuilder()
                                .addEnclosedWords('[', ']')
                                .addEnclosedWords('[', ']', "[[Lorem]]")
                                .addWord("]]]ipsum")
                                .addEnclosedWords('[', ']', "[[", "dolor", "]]")
                                .build()
                )
        );
    }

    public static Stream<Arguments> illegalEnclosingMappings() {
        return Stream.of(
                arguments(Map.of()),
                arguments(Map.of(' ', ' ')),
                arguments(Map.of('[', ']', '(', '('))
        );
    }

    @BeforeEach
    void setUp() {
        tokenizer = new SingleLevelTokenizer(Map.of('[', ']', '(', ')'));
    }

    @MethodSource("tokenizedStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void shouldTokeniseNonEmptyStrings(String input, TokenString expected) {
        var tokens = tokenizer.tokenize(input);

        assertEquals(expected, tokens);
    }

    @MethodSource("illegalEnclosingMappings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void shouldNotCreateIllegalTokenizer(Map<Character, Character> enclosingMapping) {
        assertThrows(IllegalArgumentException.class, () -> new SingleLevelTokenizer(enclosingMapping));
    }
}