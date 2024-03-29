package pro.malygin.logdenser.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import pro.malygin.logdenser.condenser.SameLengthOrderedTokenCondenser;
import pro.malygin.logdenser.condenser.SameLengthTokenCondenser;
import pro.malygin.logdenser.distance.HammingDistanceCalculator;
import pro.malygin.logdenser.token.TokenString;
import pro.malygin.logdenser.token.TokenStringGenerator;

import java.util.ArrayList;
import java.util.List;

@State(Scope.Thread)
public class TokenCondenserBenchmark {

    @Param({"1000000"})
    public int inputSize;

    @Param({"0.1", "0.3", "0.5", "0.7", "0.9"})
    public double uniqueRatio = 0.01;

    @Param({"1", "2", "3", "4", "5"})
    public int maxDistance;

    @Param({"10"})
    public int wordLength;

    @Param({"50", "100", "500"})
    public int wordsInString;

    public List<TokenString> list;
    public SameLengthTokenCondenser tokenCondenser;
    public SameLengthOrderedTokenCondenser orderedTokenCondenser;

    @Setup(Level.Trial)
    public void setUp() {
        list = new ArrayList<>();
        var tokenStringGenerator = new TokenStringGenerator();
        int copies = (int) Math.round(inputSize * uniqueRatio);
        for (int unique = 0; unique < uniqueRatio; unique++) {
            var tokenString = tokenStringGenerator.generateSimpleString(wordLength, wordsInString);
            for (int copy = 0; copy < copies; copy++) {
                list.add(tokenString);
            }
        }

        tokenCondenser = new SameLengthTokenCondenser(
                new HammingDistanceCalculator(),
                (left, right, editDistance) -> left.firstToken().equals(right.firstToken()) && editDistance.distance() <= maxDistance
        );
        orderedTokenCondenser = new SameLengthOrderedTokenCondenser(
                new HammingDistanceCalculator(),
                (left, right, editDistance) -> left.firstToken().equals(right.firstToken()) && editDistance.distance() <= maxDistance
        );
    }

    @Benchmark
    public void unsorted(Blackhole blackhole) {
        blackhole.consume(tokenCondenser.condense(list));
    }

    // as you can see in results.txt, sorted implementation performs way worse that unsorted; benchmark disabled
    public void sorted(Blackhole blackhole) {
        blackhole.consume(orderedTokenCondenser.condense(list));
    }
}
