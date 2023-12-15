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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                ),
                arguments(
                        "Lorem\u00A0ipsum dolor sit\u2007amet, consectetur\u202Fadipiscing",
                        new TokenStringBuilder()
                                .addWords("Lorem\u00A0ipsum", "dolor", "sit\u2007amet,", "consectetur\u202Fadipiscing")
                                .build()
                )
        );
    }

    public static Stream<Arguments> emptyStrings() {
        return Stream.of(
                arguments(""),
                arguments("      "),
                arguments(" \n\t\u000B\f\r\u001C\u001D\u001E\u001F  ")
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
    void should_tokenise_non_empty_strings(String input, TokenString expected) {
        var tokens = tokenizer.tokenize(input);

        assertEquals(expected, tokens);
    }

    @MethodSource("emptyStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void should_tokenise_empty_strings(String input) {
        var tokens = tokenizer.tokenize(input);

        assertTrue(tokens.isEmpty());
    }

    @MethodSource("illegalEnclosingMappings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void should_not_create_illegal_tokenizer(Map<Character, Character> enclosingMapping) {
        assertThrows(IllegalArgumentException.class, () -> new SingleLevelTokenizer(enclosingMapping));
    }
}