package pro.malygin.logdenser.condenser;

import pro.malygin.logdenser.distance.EditDistanceBuilder;
import pro.malygin.logdenser.distance.EditDistanceCalculator;
import pro.malygin.logdenser.result.FullResultBuilder;
import pro.malygin.logdenser.result.SameResults;
import pro.malygin.logdenser.token.TokenStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static pro.malygin.logdenser.distance.EditType.SUBSTITUTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SameLengthTokenCondenserTest {

    private EditDistanceCalculator editDistanceCalculator;
    private SameLengthTokenCondenser condenser;

    @BeforeEach
    void setUp() {
        editDistanceCalculator = mock();
        condenser = new SameLengthTokenCondenser(
                editDistanceCalculator,
                (left, right) -> left.firstToken().equals(right.firstToken()),
                2
        );
    }

    @Test
    void should_condense_strings() {
        var string1 = new TokenStringBuilder().addWords("Lorem", "ipsum", "sit", "amet").build();
        var string2 = new TokenStringBuilder().addWords("Lorem", "ipsumm", "sit", "amet").build();
        var string3 = new TokenStringBuilder().addWords("Lorem", "ipsu", "sit", "ame").build();

        given(editDistanceCalculator.distance(string1, string2))
                .willReturn(new EditDistanceBuilder().addEdit(1, SUBSTITUTION).build());
        given(editDistanceCalculator.distance(string1, string3))
                .willReturn(new EditDistanceBuilder().addEdit(1, SUBSTITUTION).addEdit(3, SUBSTITUTION).build());

        var results = condenser.condense(List.of(string1, string2, string3));

        var fullResult = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions("ipsum", "ipsumm", "ipsu")
                .addStatic("sit")
                .addOptions("amet", "ame")
                .build();
        var sameResults = new SameResults(fullResult, 3);
        assertIterableEquals(List.of(sameResults), results);
        assertEquals("Lorem <ipsum|ipsumm|ipsu> sit <amet|ame> {3}", sameResults.toString());
    }

    @Test
    void should_condense_strings_with_low_similarity() {
        condenser = new SameLengthTokenCondenser(
                editDistanceCalculator,
                (left, right) -> left.firstToken().equals(right.firstToken()),
                1
        );
        var string1 = new TokenStringBuilder().addWords("Lorem", "ipsum", "sit", "amet").build();
        var string2 = new TokenStringBuilder().addWords("Lorem", "ipsumm", "sit", "amet").build();
        var string3 = new TokenStringBuilder().addWords("Lorem", "ipsu", "sit", "ame").build();

        given(editDistanceCalculator.distance(string1, string2))
                .willReturn(new EditDistanceBuilder().addEdit(1, SUBSTITUTION).build());
        given(editDistanceCalculator.distance(string1, string3))
                .willReturn(new EditDistanceBuilder().addEdit(1, SUBSTITUTION).addEdit(3, SUBSTITUTION).build());

        var results = condenser.condense(List.of(string1, string2, string3));

        var fullResult1 = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions("ipsum", "ipsumm")
                .addStatics("sit", "amet")
                .build();
        var fullResult2 = new FullResultBuilder().addStatics()
                .addStatics("Lorem", "ipsu", "sit", "ame")
                .build();
        assertIterableEquals(List.of(new SameResults(fullResult1, 2), new SameResults(fullResult2, 1)), results);
    }

    @Test
    void should_condense_strings_by_first_token() {
        var string1 = new TokenStringBuilder().addWords("Lorem1", "ipsum", "sit", "amet").build();
        var string2 = new TokenStringBuilder().addWords("Lorem2", "ipsum", "sit", "amet").build();
        var string3 = new TokenStringBuilder().addWords("Lorem3", "ipsum", "sit", "amet").build();

        given(editDistanceCalculator.distance(string1, string2))
                .willReturn(new EditDistanceBuilder().addEdit(0, SUBSTITUTION).build());
        given(editDistanceCalculator.distance(string1, string3))
                .willReturn(new EditDistanceBuilder().addEdit(0, SUBSTITUTION).build());

        var results = condenser.condense(List.of(string1, string2, string3));

        var fullResult1 = new FullResultBuilder().addStatics("Lorem1", "ipsum", "sit", "amet").build();
        var fullResult2 = new FullResultBuilder().addStatics("Lorem2", "ipsum", "sit", "amet").build();
        var fullResult3 = new FullResultBuilder().addStatics("Lorem3", "ipsum", "sit", "amet").build();
        var sameResults = List.of(
                new SameResults(fullResult1, 1),
                new SameResults(fullResult2, 1),
                new SameResults(fullResult3, 1)
        );
        assertIterableEquals(sameResults, results);
    }

    @Test
    void should_condense_empty_strings() {
        condenser = new SameLengthTokenCondenser(
                editDistanceCalculator,
                (left, right) -> true,
                1
        );
        var string1 = new TokenStringBuilder().build();
        var string2 = new TokenStringBuilder().build();
        var string3 = new TokenStringBuilder().build();

        given(editDistanceCalculator.distance(string1, string2)).willReturn(new EditDistanceBuilder().build());
        given(editDistanceCalculator.distance(string1, string3)).willReturn(new EditDistanceBuilder().build());

        var results = condenser.condense(List.of(string1, string2, string3));

        var fullResult = new FullResultBuilder().build();
        assertIterableEquals(List.of(new SameResults(fullResult, 3)), results);
    }

    @Test
    void should_condense_empty_input() {
        var results = condenser.condense(List.of());

        assertIterableEquals(List.of(), results);
    }

    @Test
    void should_condense_single_input() {
        var string1 = new TokenStringBuilder().addWords("Lorem1", "ipsum", "sit", "amet").build();

        var results = condenser.condense(List.of(string1));

        var fullResult = new FullResultBuilder().addStatics("Lorem1", "ipsum", "sit", "amet").build();
        assertIterableEquals(List.of(new SameResults(fullResult, 1)), results);
    }

    @Test
    void should_throw_exception_when_two_strings_have_different_length() {
        var string1 = new TokenStringBuilder().addWords("Lorem", "ipsum", "sit", "amet").build();
        var string2 = new TokenStringBuilder().addWords("Lorem", "ipsumm", "sit").build();

        Executable executable = () -> condenser.condense(List.of(string1, string2));

        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(
                "Incorrect number of tokens. Expected 4, found: 3 in {Lorem ipsumm sit}",
                exception.getMessage()
        );
    }

    @Test
    void should_throw_exception_when_strings_have_different_length() {
        var string1 = new TokenStringBuilder().addWords("Lorem", "ipsum", "sit", "amet").build();
        var string2 = new TokenStringBuilder().addWords("Lorem", "ipsumm", "sit", "amet").build();
        var string3 = new TokenStringBuilder().addWords("Lorem", "ipsu", "sit", "amet", "dolor").build();

        given(editDistanceCalculator.distance(string1, string2))
                .willReturn(new EditDistanceBuilder().addEdit(1, SUBSTITUTION).build());

        Executable executable = () -> condenser.condense(List.of(string1, string2, string3));

        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(
                "Incorrect number of tokens. Expected 4, found: 5 in {Lorem ipsu sit amet dolor}",
                exception.getMessage()
        );
    }
}