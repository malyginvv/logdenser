package dev.mvv.input;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

/**
 * InputProcessor that extracts strings from files.
 */
public class FileInputProcessor implements InputProcessor<File> {
    @Override
    public Stream<String> process(File input) {
        try {
            return lines(input.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
