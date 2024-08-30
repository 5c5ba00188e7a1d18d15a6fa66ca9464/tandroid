package com.google.gson.internal;
/* loaded from: classes.dex */
public abstract class $Gson$Preconditions {
    public static void checkArgument(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }
}
