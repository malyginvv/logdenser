package dev.mvv.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FullResultTest {

    @Test
    void should_generate_template() {
        var fullResult1 = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions("ipsum", "ipsumm", "ipsu")
                .addStatic("sit")
                .build();
        var fullResult2 = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions("ipsum", "ipsumm", "ipsu")
                .addStatic("sit")
                .build();
        var fullResult3 = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions("ipsum", "ipsumm")
                .addStatic("sit")
                .build();
        var fullResult4 = new FullResultBuilder()
                .addStatic("Lorem")
                .addOptions()
                .addStatic("sit")
                .build();
        var fullResult5 = new FullResultBuilder()
                .addStatic("Lorem")
                .addStatic("ipsum")
                .addStatic("sit")
                .build();

        assertEquals(fullResult1.template(), fullResult2.template());
        assertEquals(fullResult1.template(), fullResult3.template());
        assertEquals(fullResult1.template(), fullResult4.template());
        assertNotEquals(fullResult1.template(), fullResult5.template());
    }
}
