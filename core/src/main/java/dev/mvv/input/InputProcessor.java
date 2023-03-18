package dev.mvv.input;

import java.util.stream.Stream;

public interface InputProcessor<T> {
    Stream<String> process(T input);
}
