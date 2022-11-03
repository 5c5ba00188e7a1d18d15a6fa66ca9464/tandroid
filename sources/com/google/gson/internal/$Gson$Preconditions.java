package com.google.gson.internal;
/* loaded from: classes.dex */
public final class $Gson$Preconditions {
    public static void checkArgument(boolean z) {
        if (z) {
            return;
        }
        throw new IllegalArgumentException();
    }
}
