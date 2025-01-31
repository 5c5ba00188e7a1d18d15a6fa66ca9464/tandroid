package com.google.android.recaptcha.internal;

import java.util.ArrayList;
import java.util.List;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.CoroutineScopeKt;

/* loaded from: classes.dex */
public final class zzg {
    private final List zza;

    /* JADX WARN: Multi-variable type inference failed */
    public zzg() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public /* synthetic */ zzg(List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
        List emptyList = CollectionsKt__CollectionsKt.emptyList();
        ArrayList arrayList = new ArrayList();
        this.zza = arrayList;
        arrayList.addAll(emptyList);
    }

    public final Object zza(String str, long j, Continuation continuation) {
        return CoroutineScopeKt.coroutineScope(new zzc(this, str, j, null), continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zzb(long j, zzoe zzoeVar, Continuation continuation) {
        zzd zzdVar;
        Object coroutine_suspended;
        int i;
        if (continuation instanceof zzd) {
            zzdVar = (zzd) continuation;
            int i2 = zzdVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzdVar.zzc = i2 - Integer.MIN_VALUE;
                Object obj = zzdVar.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzdVar.zzc;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    zzf zzfVar = new zzf(this, j, zzoeVar, null);
                    zzdVar.zzc = 1;
                    obj = CoroutineScopeKt.coroutineScope(zzfVar, zzdVar);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                }
                return ((Result) obj).unbox-impl();
            }
        }
        zzdVar = new zzd(this, continuation);
        Object obj2 = zzdVar.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzdVar.zzc;
        if (i != 0) {
        }
        return ((Result) obj2).unbox-impl();
    }

    public final List zzc() {
        return this.zza;
    }

    public final void zzd(zza zzaVar) {
        this.zza.add(zzaVar);
    }
}
