package com.optymyze.codingchallenge.util;

public final class StringUtil {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
