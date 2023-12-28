package pro.malygin.logdenser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TokenStringGenerator {

    private final Random random;
    private final StringGenerator stringGenerator;

    public TokenStringGenerator(Random random) {
        this.random = random;
        stringGenerator = new StringGenerator(random);
    }

    public TokenStringGenerator() {
        this(new Random());
    }

    public TokenString generateSimpleString(int wordLength, int length) {
        List<Token> tokens = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            tokens.add(new Word(stringGenerator.generateWord(wordLength)));
        }
        return new TokenString(tokens);
    }
}
