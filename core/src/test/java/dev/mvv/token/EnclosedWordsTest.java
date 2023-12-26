package dev.mvv.token;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EnclosedWordsTest {

    @Test
    void should_respect_object_contract() {
        var enclosedWords1 = new EnclosedWords(Stream.of("dolor", "sit", "amet").map(Word::new).toList(), '[', ']');
        var enclosedWords2 = new EnclosedWords(Stream.of("dolor", "sit", "amet").map(Word::new).toList(), '[', ']');
        var enclosedWords3 = new EnclosedWords(Stream.of("dolor", "sit", "ipsum").map(Word::new).toList(), '[', ']');
        var enclosedWords4 = new EnclosedWords(Stream.of("dolor", "sit", "amet").map(Word::new).toList(), '(', ')');
        var empty = new EnclosedWords(Collections.emptyList(), ' ', ' ');

        assertEquals(enclosedWords1, enclosedWords2);
        assertEquals(enclosedWords1.hashCode(), enclosedWords2.hashCode());
        assertNotEquals(enclosedWords1, enclosedWords3);
        assertNotEquals(enclosedWords1, enclosedWords4);
        assertNotEquals(enclosedWords1, empty);
        assertNotEquals(enclosedWords1, null);
        assertNotEquals(enclosedWords1, "[dolor sit amet]");
    }
}