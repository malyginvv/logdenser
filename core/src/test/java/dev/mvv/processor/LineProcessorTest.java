package dev.mvv.processor;

import dev.mvv.token.TokenString;
import dev.mvv.token.TokenStringBuilder;
import dev.mvv.token.Word;
import dev.mvv.tokenizer.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.function.UnaryOperator.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LineProcessorTest {

    private Tokenizer tokenizer;
    private LineProcessor lineProcessor;

    @BeforeEach
    void setUp() {
        tokenizer = mock();
        lineProcessor = new LineProcessor(identity(), tokenizer);
    }

    @Test
    void should_process_lines() {
        var tokenString = new TokenStringBuilder().addWord("test").build();
        given(tokenizer.tokenize("test")).willReturn(tokenString);

        var result = lineProcessor.process("test");

        assertEquals(new TokenString(List.of(new Word("test"))), result);
    }
}