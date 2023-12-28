package pro.malygin.logdenser.token;

import java.util.Random;

public class StringGenerator {

    private static final int MIN_CODE = 'a';
    private static final int MAX_CODE = 'z';

    private final Random random;

    public StringGenerator(Random random) {
        this.random = random;
    }

    public StringGenerator() {
        this(new Random());
    }

    public String generateWord(int length) {
        char[] word = new char[length];
        for (int i = 0; i < length; i++) {
            word[i] = (char) random.nextInt(MIN_CODE, MAX_CODE + 1);
        }
        return String.valueOf(word);
    }
}
