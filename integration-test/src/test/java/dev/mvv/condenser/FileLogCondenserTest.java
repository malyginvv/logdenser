package dev.mvv.condenser;

import dev.mvv.distance.HammingDistanceCalculator;
import dev.mvv.filter.WhitespaceQuotFilter;
import dev.mvv.input.FileInputProcessor;
import dev.mvv.processor.LineProcessor;
import dev.mvv.tokenizer.SingleLevelTokenizer;
import dev.mvv.transformer.CompositeTransformer;
import dev.mvv.transformer.FirstWordsCutter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static dev.mvv.test.ResourceUtil.fromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileLogCondenserTest {

    private LogCondenser<File> logCondenser;

    @BeforeEach
    void setUp() {
        logCondenser = new LogCondenser<>(
                new FileInputProcessor(),
                new WhitespaceQuotFilter(),
                new LineProcessor(
                        new CompositeTransformer(List.of(new FirstWordsCutter(2))),
                        new SingleLevelTokenizer(Map.of('[', ']', '(', ')'))
                ),
                new SameLengthTokenCondenser(
                        new HammingDistanceCalculator(),
                        (left, right) -> left.firstToken().equals(right.firstToken()),
                        2
                )
        );
    }

    @Test
    void should_condense_maven_log() {
        var results = logCondenser.condense(fromResource("maven.compile.log"));

        assertEquals(23, results.size());
    }
}
