package dev.mvv.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.List;

import static dev.mvv.test.ResourceUtil.fromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileInputProcessorTest {

    private FileInputProcessor fileInputProcessor;

    @BeforeEach
    void setUp() {
        fileInputProcessor = new FileInputProcessor();
    }

    @Test
    void should_process_file() {
        var stream = fileInputProcessor.process(fromResource("build.txt"));

        assertEquals(
                List.of(
                        "> Task :core:compileJava UP-TO-DATE",
                        "> Task :core:processResources NO-SOURCE",
                        "> Task :core:classes UP-TO-DATE",
                        "> Task :core:jar UP-TO-DATE",
                        "> Task :core:compileTestFixturesJava UP-TO-DATE",
                        "> Task :core:processTestFixturesResources NO-SOURCE",
                        "> Task :core:testFixturesClasses UP-TO-DATE",
                        "> Task :core:testFixturesJar UP-TO-DATE",
                        "> Task :core:compileTestJava",
                        "> Task :core:processTestResources NO-SOURCE",
                        "> Task :core:testClasses",
                        "> Task :core:test"
                ),
                stream.toList());
    }

    @Test
    void should_throw_exception_when_file_is_unreadable() {
        Executable executable = () -> fileInputProcessor.process(new File("fail"));

        assertThrows(RuntimeException.class, executable);
    }
}