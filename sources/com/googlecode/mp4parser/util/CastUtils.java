package com.googlecode.mp4parser.util;

/* loaded from: classes.dex */
public abstract class CastUtils {
    public static int l2i(long j) {
        if (j <= 2147483647L && j >= -2147483648L) {
            return (int) j;
        }
        throw new RuntimeException("A cast to int has gone wrong. Please contact the mp4parser discussion group (" + j + ")");
    }
}
