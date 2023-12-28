package pro.malygin.logdenser.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WordTest {

    @Test
    void should_respect_object_contract() {
        var word1 = new Word("Lorem");
        var word2 = new Word("Lorem");
        var word3 = new Word("ipsum");
        var empty = new Word("");

        assertEquals(word1, word2);
        assertEquals(word1.hashCode(), word2.hashCode());
        assertNotEquals(word1, word3);
        assertNotEquals(word1, empty);
        assertNotEquals(word1, null);
    }
}