package com.google.android.recaptcha.internal;

import android.content.ContentValues;
import java.util.List;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
final class zzbl extends SuspendLambda implements Function2 {
    final /* synthetic */ zzbm zza;
    final /* synthetic */ zzpd zzb;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbl(zzbm zzbmVar, zzpd zzpdVar, Continuation continuation) {
        super(2, continuation);
        this.zza = zzbmVar;
        this.zzb = zzpdVar;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzbl(this.zza, this.zzb, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzbl) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        zzaz zzazVar;
        zzaz zzazVar2;
        zzaz zzazVar3;
        zzaz zzazVar4;
        zzaz zzazVar5;
        List take;
        zzaz zzazVar6;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ResultKt.throwOnFailure(obj);
        zzbm zzbmVar = this.zza;
        zzpd zzpdVar = this.zzb;
        synchronized (zzbh.class) {
            try {
                zzazVar = zzbmVar.zze;
                if (zzazVar != null) {
                    byte[] zzd = zzpdVar.zzd();
                    zzba zzbaVar = new zzba(zzfy.zzg().zzi(zzd, 0, zzd.length), System.currentTimeMillis(), 0);
                    zzazVar2 = zzbmVar.zze;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ss", zzbaVar.zzc());
                    contentValues.put("ts", Long.valueOf(zzbaVar.zzb()));
                    zzazVar2.getWritableDatabase().insert("ce", null, contentValues);
                    zzazVar3 = zzbmVar.zze;
                    int zzb = zzazVar3.zzb() - 500;
                    if (zzb > 0) {
                        zzazVar5 = zzbmVar.zze;
                        take = CollectionsKt___CollectionsKt.take(zzazVar5.zzd(), zzb);
                        zzazVar6 = zzbmVar.zze;
                        zzazVar6.zza(take);
                    }
                    zzazVar4 = zzbmVar.zze;
                    if (zzazVar4.zzb() >= 20) {
                        zzbmVar.zzg();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return Unit.INSTANCE;
    }
}
