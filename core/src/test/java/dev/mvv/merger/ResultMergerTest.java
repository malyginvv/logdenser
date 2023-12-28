package dev.mvv.merger;

import dev.mvv.result.FullResultBuilder;
import dev.mvv.result.SameResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ResultMergerTest {

    private ResultMerger resultMerger;

    @BeforeEach
    void setUp() {
        resultMerger = new ResultMerger();
    }

    @Test
    void should_merge_results() {
        var input = List.of(
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").build(), 2),
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").build(), 3),
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsumm").build(), 4),
                new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsum").build(), 3),
                new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsumm").build(), 3),
                new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsum").build(), 1)
        );

        var merged = resultMerger.merge(input);

        assertIterableEquals(
                List.of(
                        new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu", "ipsumm").build(), 9),
                        new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsum").build(), 4),
                        new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsumm").build(), 3)
                ),
                merged);
    }

    @Test
    void should_merge_unique_results() {
        var input = List.of(
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").addStatic("dolor").build(), 2),
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").addStatic("dolo").build(), 3),
                new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsumm").addStatic("dol").build(), 4),
                new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsum").build(), 3),
                new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsumm").build(), 3)
        );

        var merged = resultMerger.merge(input);

        assertIterableEquals(
                List.of(
                        new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").addStatic("dolor").build(), 2),
                        new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsu").addStatic("dolo").build(), 3),
                        new SameResults(new FullResultBuilder().addStatics("Lorem").addOptions("ipsum", "ipsumm").addStatic("dol").build(), 4),
                        new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsum").build(), 3),
                        new SameResults(new FullResultBuilder().addStatics("Lorem", "ipsumm").build(), 3)
                ),
                merged);
    }

    @Test
    void should_merge_empty_results() {
        List<SameResults> input = emptyList();

        var merged = resultMerger.merge(input);

        assertIterableEquals(List.of(), merged);
    }
}