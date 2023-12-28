package pro.malygin.logdenser.mvv.transformer;

import java.util.function.UnaryOperator;

import static java.lang.Character.isSpaceChar;

public class FirstWordsCutter implements UnaryOperator<String> {

    private final int spaceCount;

    public FirstWordsCutter(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    @Override
    public String apply(String s) {
        int spaces = 0;
        var chars = s.toCharArray();
        boolean insideSpace = false;
        int cutPoint = -1;
        for (int i = 0; i < chars.length; i++) {
            if (isSpaceChar(chars[i])) {
                if (!insideSpace) {
                    insideSpace = true;
                }
            } else {
                insideSpace = false;
                spaces++;
                if (spaces == spaceCount) {
                    cutPoint = i;
                    break;
                }
            }
        }

        return cutPoint >= 0 ? s.substring(cutPoint) : s;
    }
}
