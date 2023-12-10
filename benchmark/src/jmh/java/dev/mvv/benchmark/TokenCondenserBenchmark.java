package dev.mvv.benchmark;

import dev.mvv.condenser.SameLengthOrderedTokenCondenser;
import dev.mvv.condenser.SameLengthTokenCondenser;
import dev.mvv.distance.HammingDistanceCalculator;
import dev.mvv.token.TokenString;
import dev.mvv.token.TokenStringGenerator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

@State(Scope.Thread)
public class TokenCondenserBenchmark {

    @Param({"1000", "10000", "100000", "1000000"})
    public int inputSize;

    @Param({"true", "false"})
    public boolean shuffled;

    @Param({"0.01", "0.05", "0.1"})
    public double uniqueRatio = 0.01;

    @Param({"1", "2"})
    public int maxDistance;

    public List<TokenString> list;
    public SameLengthTokenCondenser tokenCondenser;
    public SameLengthOrderedTokenCondenser orderedTokenCondenser;

    @Setup(Level.Trial)
    public void setUp() {
        list = new ArrayList<>();
        var tokenStringGenerator = new TokenStringGenerator();
        int copies = (int) Math.round(inputSize * uniqueRatio);
        for (int unique = 0; unique < uniqueRatio; unique++) {
            var tokenString = tokenStringGenerator.generateSimpleString(10, 10);
            for (int copy = 0; copy < copies; copy++) {
                list.add(tokenString);
            }
        }
        if (shuffled) {
            shuffle(list);
        }

        tokenCondenser = new SameLengthTokenCondenser(
                new HammingDistanceCalculator(),
                (left, right) -> left.firstToken().equals(right.firstToken()),
                maxDistance
        );
        orderedTokenCondenser = new SameLengthOrderedTokenCondenser(
                new HammingDistanceCalculator(),
                (left, right) -> left.firstToken().equals(right.firstToken()),
                maxDistance
        );
    }

    @Benchmark
    public void unsorted(Blackhole blackhole) {
        blackhole.consume(tokenCondenser.condense(list));
    }

    @Benchmark
    public void sorted(Blackhole blackhole) {
        blackhole.consume(orderedTokenCondenser.condense(list));
    }
}
