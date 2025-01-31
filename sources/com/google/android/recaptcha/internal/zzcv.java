package com.google.android.recaptcha.internal;

import java.util.Collection;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.Charsets;

/* loaded from: classes.dex */
public final class zzcv implements zzdd {
    public static final zzcv zza = new zzcv();

    private zzcv() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        String joinToString$default;
        String str;
        if (zzpqVarArr.length != 1) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Object)) {
            zza2 = null;
        }
        if (zza2 == null) {
            throw new zzae(4, 5, null);
        }
        if (zza2 instanceof int[]) {
            joinToString$default = ArraysKt___ArraysKt.joinToString$default((int[]) zza2, (CharSequence) ",", (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
        } else {
            if (zza2 instanceof byte[]) {
                str = new String((byte[]) zza2, Charsets.UTF_8);
            } else if (zza2 instanceof long[]) {
                joinToString$default = ArraysKt___ArraysKt.joinToString$default((long[]) zza2, (CharSequence) ",", (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
            } else if (zza2 instanceof short[]) {
                joinToString$default = ArraysKt___ArraysKt.joinToString$default((short[]) zza2, (CharSequence) ",", (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
            } else if (zza2 instanceof float[]) {
                joinToString$default = ArraysKt___ArraysKt.joinToString$default((float[]) zza2, (CharSequence) ",", (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
            } else if (zza2 instanceof double[]) {
                joinToString$default = ArraysKt___ArraysKt.joinToString$default((double[]) zza2, ",", "[", "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
            } else if (zza2 instanceof char[]) {
                str = new String((char[]) zza2);
            } else if (zza2 instanceof Object[]) {
                joinToString$default = ArraysKt___ArraysKt.joinToString$default((Object[]) zza2, ",", "[", "]", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
            } else {
                if (!(zza2 instanceof Collection)) {
                    throw new zzae(4, 5, null);
                }
                joinToString$default = CollectionsKt___CollectionsKt.joinToString$default((Iterable) zza2, ",", "[", "]", 0, null, null, 56, null);
            }
            joinToString$default = str;
        }
        zzcjVar.zzc().zzf(i, joinToString$default);
    }
}
