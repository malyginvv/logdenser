package pro.malygin.logdenser.condenser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.malygin.logdenser.distance.HammingDistanceCalculator;
import pro.malygin.logdenser.input.FileInputProcessor;
import pro.malygin.logdenser.mvv.filter.WhitespaceQuotFilter;
import pro.malygin.logdenser.mvv.transformer.FirstWordsCutter;
import pro.malygin.logdenser.processor.LineProcessor;
import pro.malygin.logdenser.tokenizer.SingleLevelTokenizer;

import java.io.File;
import java.util.Map;

import static java.util.function.UnaryOperator.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pro.malygin.logdenser.test.ResourceUtil.fromResource;

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
