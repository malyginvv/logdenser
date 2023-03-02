package dev.mvv.token;

public enum Bracket {
    NONE(' ', ' '),
    ROUND('(', ')'),
    SQUARE('[', ']');

    public final char opening;
    public final char closing;

    Bracket(char opening, char closing) {
        this.opening = opening;
        this.closing = closing;
    }

    public static Bracket byOpening(char c) {
        return switch (c) {
            case '[' -> SQUARE;
            case '(' -> ROUND;
            default -> NONE;
        };
    }

    public static Bracket byClosing(char c) {
        return switch (c) {
            case ']' -> SQUARE;
            case ')' -> ROUND;
            default -> NONE;
        };
    }
}
