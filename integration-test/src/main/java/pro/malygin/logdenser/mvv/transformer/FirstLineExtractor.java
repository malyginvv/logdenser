package pro.malygin.logdenser.mvv.transformer;

import java.util.function.UnaryOperator;

public class FirstLineExtractor implements UnaryOperator<String> {

    @Override
    public String apply(String s) {
        var newLineIndex = s.indexOf("\n");
        return newLineIndex > 0 ? s.substring(0, newLineIndex) : s;
    }
}
