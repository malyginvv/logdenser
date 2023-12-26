package dev.mvv.token;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TokenStringTest {

    @Test
    void should_respect_object_contract() {
        var tokenString1 = new TokenStringBuilder()
                .addWords("Lorem", "ipsum")
                .addEnclosedWords('[', ']', "dolor", "sit", "amet")
                .addEnclosedWords('(', ')', "100", "ms")
                .build();
        var tokenString2 = new TokenString(List.of(
                new Word("Lorem"),
                new Word("ipsum"),
                new EnclosedWords(Stream.of("dolor", "sit", "amet").map(Word::new).toList(), '[', ']'),
                new EnclosedWords(Stream.of("100", "ms").map(Word::new).toList(), '(', ')')
        ));
        var empty = new TokenString(emptyList());

        assertEquals(tokenString1, tokenString2);
        assertEquals(tokenString1.hashCode(), tokenString2.hashCode());
        assertNotEquals(tokenString1, empty);
    }
}