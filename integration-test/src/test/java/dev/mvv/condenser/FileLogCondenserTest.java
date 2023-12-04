package dev.mvv.condenser;

import dev.mvv.distance.HammingDistanceCalculator;
import dev.mvv.filter.WhitespaceQuotFilter;
import dev.mvv.input.FileInputProcessor;
import dev.mvv.processor.LineProcessor;
import dev.mvv.tokenizer.SingleLevelTokenizer;
import dev.mvv.transformer.FirstWordsCutter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static dev.mvv.test.ResourceUtil.fromResource;
import static java.util.function.UnaryOperator.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileLogCondenserTest {

    private LogCondenser<File> logCondenser;

    @BeforeEach
    void setUp() {
        logCondenser = buildCondenser(2);
    }

    @Test
    void should_condense_maven_log() {
        var results = logCondenser.condense(fromResource("maven.compile.log.txt"));

        assertEquals(23, results.size());
    }

    @Test
    void should_condense_build_log() {
        logCondenser = buildCondenser(0);

        var results = logCondenser.condense(fromResource("spring-core-test.log.txt"));

        System.out.println(results);
        assertEquals(6, results.size());
    }

    private LogCondenser<File> buildCondenser(int firstWordsToCut) {
        return new LogCondenser<>(
                new FileInputProcessor(),
                new WhitespaceQuotFilter(),
                new LineProcessor(
                        firstWordsToCut == 0 ? identity() : new FirstWordsCutter(firstWordsToCut),
                        new SingleLevelTokenizer(Map.of('[', ']', '(', ')'))
                ),
                new SameLengthTokenCondenser(
                        new HammingDistanceCalculator(),
                        (left, right) -> left.firstToken().equals(right.firstToken()),
                        2
                )
        );
    }
}
