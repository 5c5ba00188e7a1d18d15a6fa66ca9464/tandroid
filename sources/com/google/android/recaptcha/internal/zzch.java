package com.google.android.recaptcha.internal;

import java.lang.reflect.Method;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public final class zzch extends zzce {
    private final zzcg zza;
    private final String zzb;

    public zzch(zzcg zzcgVar, String str, Object obj) {
        super(obj);
        this.zza = zzcgVar;
        this.zzb = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0010, code lost:
    
        r2 = kotlin.collections.ArraysKt___ArraysJvmKt.asList(r3);
     */
    @Override // com.google.android.recaptcha.internal.zzce
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zza(Object obj, Method method, Object[] objArr) {
        List emptyList;
        if (!Intrinsics.areEqual(method.getName(), this.zzb)) {
            return false;
        }
        zzcg zzcgVar = this.zza;
        if (objArr == null || emptyList == null) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
        }
        zzcgVar.zzb(emptyList);
        return true;
    }
}
