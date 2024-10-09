package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Objects;

/* loaded from: classes.dex */
abstract class zzal {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, String str) {
        if (i >= 0) {
            return i;
        }
        throw new IllegalArgumentException(str + " cannot be negative but was: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zzb(Object obj, Object obj2) {
        if (obj == null) {
            Objects.toString(obj2);
            throw new NullPointerException("null key in entry: null=".concat(String.valueOf(obj2)));
        }
        if (obj2 != null) {
            return;
        }
        throw new NullPointerException("null value in entry: " + obj + "=null");
    }
}
