package com.google.android.recaptcha.internal;

import java.util.Iterator;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* loaded from: classes.dex */
public final class zzef {
    private List zza = CollectionsKt__CollectionsKt.emptyList();

    public final long zza(long[] jArr) {
        List list;
        List plus;
        List list2 = this.zza;
        list = ArraysKt___ArraysKt.toList(jArr);
        plus = CollectionsKt___CollectionsKt.plus(list2, list);
        Iterator it = plus.iterator();
        if (!it.hasNext()) {
            throw new UnsupportedOperationException("Empty collection can't be reduced.");
        }
        Object next = it.next();
        while (it.hasNext()) {
            next = Long.valueOf(((Number) it.next()).longValue() ^ ((Number) next).longValue());
        }
        return ((Number) next).longValue();
    }

    public final void zzb(long[] jArr) {
        List list;
        list = ArraysKt___ArraysKt.toList(jArr);
        this.zza = list;
    }
}
