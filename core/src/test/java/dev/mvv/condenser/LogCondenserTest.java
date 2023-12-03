package dev.mvv.condenser;

import dev.mvv.input.InputProcessor;
import dev.mvv.processor.LineProcessor;
import dev.mvv.result.FullResultBuilder;
import dev.mvv.result.SameResults;
import dev.mvv.token.TokenStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LogCondenserTest {

    private InputProcessor<String> inputProcessor;
    private Predicate<String> filter;
    private LineProcessor lineProcessor;
    private TokenCondenser tokenCondenser;
    private LogCondenser<String> logCondenser;

    @BeforeEach
    void setUp() {
        inputProcessor = mock();
        filter = mock();
        lineProcessor = mock();
        tokenCondenser = mock();
        logCondenser = new LogCondenser<>(inputProcessor, filter, lineProcessor, tokenCondenser);
    }

    @Test
    void should_condense_by_size() {
        var first = new TokenStringBuilder().addWords("Lorem", "ipsum").build();
        var second = new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build();
        var third = new TokenStringBuilder().addWords("Lorem", "ipsum").addEnclosedWords('[', ']', "amet", "sit").build();
        var fourth = new TokenStringBuilder().build();
        var firstResult = new FullResultBuilder().addStatics("Lorem", "ipsum").build();
        var secondResult = new FullResultBuilder().addStatics("Lorem", "ipsum").addOptions("sit", "[amet sit]").build();
        given(inputProcessor.process(any())).willReturn(Stream.of("first", "second", "third", "fourth"));
        given(filter.test(anyString())).willReturn(true);
        given(lineProcessor.process(anyString())).willReturn(first).willReturn(second).willReturn(third).willReturn(fourth);
        given(tokenCondenser.condense(List.of(first))).willReturn(List.of(new SameResults(firstResult, 1)));
        given(tokenCondenser.condense(List.of(second, third))).willReturn(List.of(new SameResults(secondResult, 2)));

        var results = logCondenser.condense("test");

        assertIterableEquals(
                List.of(new SameResults(firstResult, 1), new SameResults(secondResult, 2)),
                results);
    }
}