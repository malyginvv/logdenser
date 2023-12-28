package pro.malygin.logdenser.processor;

import pro.malygin.logdenser.token.TokenString;
import pro.malygin.logdenser.token.TokenStringBuilder;
import pro.malygin.logdenser.token.Word;
import pro.malygin.logdenser.tokenizer.Tokenizer;
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