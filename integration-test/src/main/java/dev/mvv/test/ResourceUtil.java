package dev.mvv.test;

import java.io.File;
import java.net.URISyntaxException;

import static java.util.Objects.requireNonNull;

public class ResourceUtil {

    public static File fromResource(String s) {
        var url = ResourceUtil.class.getClassLoader().getResource(s);
        try {
            return new File(requireNonNull(url).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
