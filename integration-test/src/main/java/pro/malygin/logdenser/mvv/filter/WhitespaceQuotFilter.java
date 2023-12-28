package pro.malygin.logdenser.mvv.filter;

import java.util.function.Predicate;

public class WhitespaceQuotFilter implements Predicate<String> {

    @Override
    public boolean test(String s) {
        return !s.startsWith(" ")
                && !s.startsWith("\t")
                && !s.startsWith("\"")
                && !s.startsWith("'");
    }
}
