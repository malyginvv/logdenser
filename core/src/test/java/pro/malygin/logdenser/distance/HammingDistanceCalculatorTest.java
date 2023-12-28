package pro.malygin.logdenser.distance;

import pro.malygin.logdenser.token.TokenString;
import pro.malygin.logdenser.token.TokenStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static pro.malygin.logdenser.distance.EditType.DELETION;
import static pro.malygin.logdenser.distance.EditType.INSERTION;
import static pro.malygin.logdenser.distance.EditType.SUBSTITUTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HammingDistanceCalculatorTest {

    private HammingDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HammingDistanceCalculator();
    }

    @MethodSource("tokenStrings")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void should_calculate_distance(TokenString first, TokenString second, EditDistance expectedDistance) {
        var distance = calculator.distance(first, second);

        assertEquals(expectedDistance, distance);
    }

    public static Stream<Arguments> tokenStrings() {
        return Stream.of(
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum").build(),
                        new TokenStringBuilder().addWords("Lorem", "ipsum").build(),
                        new EditDistanceBuilder().build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build(),
                        new TokenStringBuilder().addWords("Lorem", "ipsum").build(),
                        new EditDistanceBuilder().addEdit(2, DELETION).build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum").build(),
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build(),
                        new EditDistanceBuilder().addEdit(2, INSERTION).build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build(),
                        new TokenStringBuilder().addWords("Lorem", "amet", "sit").build(),
                        new EditDistanceBuilder().addEdit(1, SUBSTITUTION).build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build(),
                        new TokenStringBuilder().addWords("Lorem", "sit", "amet").build(),
                        new EditDistanceBuilder().addEdit(1, SUBSTITUTION).addEdit(2, SUBSTITUTION).build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum", "sit").build(),
                        new TokenStringBuilder().addWords("Lorem", "ipsum").addEnclosedWords('(', ')', "test", "one").build(),
                        new EditDistanceBuilder().addEdit(2, SUBSTITUTION).build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum").addEnclosedWords('(', ')', "test", "one").build(),
                        new TokenStringBuilder().addWords("Lorem", "ipsum").addEnclosedWords('(', ')', "test", "one").build(),
                        new EditDistanceBuilder().build()
                ),
                arguments(
                        new TokenStringBuilder().addWords("Lorem", "ipsum").addEnclosedWords('(', ')', "test", "one").build(),
                        new TokenStringBuilder().build(),
                        new EditDistanceBuilder().addEdit(0, DELETION).addEdit(1, DELETION).addEdit(2, DELETION).build()
                )
        );
    }
}