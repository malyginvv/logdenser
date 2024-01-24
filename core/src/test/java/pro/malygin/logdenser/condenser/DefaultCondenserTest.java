package pro.malygin.logdenser.condenser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.malygin.logdenser.result.FullResultBuilder;
import pro.malygin.logdenser.result.SameResults;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DefaultCondenserTest {

    private DefaultCondenser defaultCondenser;

    @BeforeEach
    void setUp() {
        defaultCondenser = new DefaultCondenser((left, right, editDistance) -> editDistance.distance() < 2);
    }

    @Test
    void should_condense_by_size() {
        var firstResult = new FullResultBuilder()
                .addStatics("Lorem")
                .addOptions("ipsum", "(amet)")
                .build();
        var secondResult = new FullResultBuilder().addStatics("Lore", "mipsum")
                .build();
        var thirdResult = new FullResultBuilder()
                .addStatics("Lorem", "ipsum")
                .addOptions("sit", "[amet sit]", "{amet sit dolor}")
                .build();

        var results = defaultCondenser.condense(List.of(
                "Lorem ipsum",
                "Lorem ipsum sit",
                "Lorem ipsum [amet sit]",
                " ",
                "Lorem ipsum {amet sit dolor}",
                "Lorem (amet)",
                "Lore mipsum"
        ));

        assertIterableEquals(
                List.of(
                        new SameResults(firstResult, 2),
                        new SameResults(secondResult, 1),
                        new SameResults(thirdResult, 3)
                ),
                results);
    }
}