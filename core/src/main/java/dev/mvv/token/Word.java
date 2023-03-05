package dev.mvv.token;

public record Word(String word) implements Token {

    @Override
    public String content() {
        return word;
    }
}
