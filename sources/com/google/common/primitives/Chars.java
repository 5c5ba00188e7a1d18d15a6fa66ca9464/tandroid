package com.google.common.primitives;

import com.google.common.base.Preconditions;
/* loaded from: classes.dex */
public abstract class Chars {
    public static char checkedCast(long j) {
        char c = (char) j;
        Preconditions.checkArgument(((long) c) == j, "Out of range: %s", j);
        return c;
    }

    public static boolean contains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    public static char fromBytes(byte b, byte b2) {
        return (char) ((b << 8) | (b2 & 255));
    }
}
