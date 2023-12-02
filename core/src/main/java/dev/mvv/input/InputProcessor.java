package dev.mvv.input;

import java.util.stream.Stream;

/**
 * A contract for processing input of generic type T and transforming it into a stream of strings.
 *
 * @param <T> The generic type representing the input data to be processed.
 */
public interface InputProcessor<T> {

    /**
     * Processes the input and converts it into a stream strings.
     *
     * @param input The input data to be processed.
     * @return A stream of strings extracted or derived from the input.
     */
    Stream<String> process(T input);
}
