package dev.mvv.transformer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CompositeTransformerTest {

    private CompositeTransformer compositeTransformer;

    @BeforeEach
    void setUp() {
        compositeTransformer = new CompositeTransformer(List.of(
                s -> s.replace('x', '*'),
                s -> {
                    var index = s.indexOf(' ');
                    return index >= 0 ? s.substring(index + 1) : s;
                }
        ));
    }

    @MethodSource("transformations")
    @ParameterizedTest(name = INDEX_PLACEHOLDER)
    void should_apply_transformers(String source, String expected) {
        var result = compositeTransformer.apply(source);

        assertEquals(expected, result);
    }


    public static Stream<Arguments> transformations() {
        return Stream.of(
                arguments("First second third", "second third"),
                arguments("[INFO] First second third", "First second third"),
                arguments("[INFO] First xxxxxx third", "First ****** third"),
                arguments("xyz", "*yz"),
                arguments("nochanges", "nochanges")
        );
    }
}