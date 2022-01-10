package com.nibado.example.fileservice;

import java.security.SecureRandom;
import java.util.Random;

public final class IdGenerator {
    private static Random RANDOM = new SecureRandom();
    private static char[] DICT = new char[26 + 26 + 10 + 2];
    private IdGenerator() {}

    static {
        var i = 0;

        for(int j = 0;j < 10;j++) {
            DICT[i++] = (char)('0' + j);
        }
        for(int j = 0;j < 26;j++) {
            DICT[i++] = (char)('A' + j);
            DICT[i++] = (char)('a' + j);
        }
        DICT[i++] = '-';
        DICT[i] = '_';
    }

    public static String generate() {
        return generate(21);
    }

    public static String generate(int length) {
        var builder = new StringBuilder(length);

        for(var i = 0;i < length;i++) {
            builder.append(DICT[RANDOM.nextInt(DICT.length)]);
        }

        return builder.toString();
    }
}
