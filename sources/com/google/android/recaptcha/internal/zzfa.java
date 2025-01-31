package com.google.android.recaptcha.internal;

import android.os.Build;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;

/* loaded from: classes.dex */
public final class zzfa {
    public static final zzfa zza = new zzfa();

    private zzfa() {
    }

    public static final Map zza() {
        Map mutableMapOf;
        mutableMapOf = MapsKt__MapsKt.mutableMapOf(TuplesKt.to(-4, zzl.zzz), TuplesKt.to(-12, zzl.zzA), TuplesKt.to(-6, zzl.zzv), TuplesKt.to(-11, zzl.zzx), TuplesKt.to(-13, zzl.zzB), TuplesKt.to(-14, zzl.zzC), TuplesKt.to(-2, zzl.zzw), TuplesKt.to(-7, zzl.zzD), TuplesKt.to(-5, zzl.zzE), TuplesKt.to(-9, zzl.zzF), TuplesKt.to(-8, zzl.zzP), TuplesKt.to(-15, zzl.zzy), TuplesKt.to(-1, zzl.zzG), TuplesKt.to(-3, zzl.zzI), TuplesKt.to(-10, zzl.zzJ));
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            mutableMapOf.put(-16, zzl.zzH);
        }
        if (i >= 27) {
            mutableMapOf.put(1, zzl.zzL);
            mutableMapOf.put(2, zzl.zzM);
            mutableMapOf.put(0, zzl.zzN);
            mutableMapOf.put(3, zzl.zzO);
        }
        if (i >= 29) {
            mutableMapOf.put(4, zzl.zzK);
        }
        return mutableMapOf;
    }
}
