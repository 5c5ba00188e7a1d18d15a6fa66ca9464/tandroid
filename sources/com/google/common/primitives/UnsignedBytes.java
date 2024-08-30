package com.google.common.primitives;
/* loaded from: classes.dex */
public abstract class UnsignedBytes {
    public static int toInt(byte b) {
        return b & 255;
    }
}
