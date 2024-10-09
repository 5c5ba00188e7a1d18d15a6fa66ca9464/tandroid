package com.google.firebase.components;

/* loaded from: classes.dex */
public abstract class Preconditions {
    public static void checkArgument(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static Object checkNotNull(Object obj) {
        obj.getClass();
        return obj;
    }

    public static Object checkNotNull(Object obj, String str) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(str);
    }

    public static void checkState(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }
}
