package dev.mvv.distance;

import dev.mvv.token.TokenString;

public interface EditDistanceCalculator {
    int distance(TokenString first, TokenString second);
}
