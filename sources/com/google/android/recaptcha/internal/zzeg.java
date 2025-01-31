package com.google.android.recaptcha.internal;

import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.UInt;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* loaded from: classes.dex */
public final class zzeg implements zzee {
    private final zzef zza;
    private final zzed zzb;

    public zzeg(zzef zzefVar, zzed zzedVar) {
        this.zza = zzefVar;
        this.zzb = zzedVar;
    }

    private final zzpf zzb(String str, List list) {
        long[] longArray;
        zzea zzeaVar;
        if (str.length() == 0) {
            throw new zzae(3, 17, null);
        }
        try {
            zzef zzefVar = this.zza;
            longArray = CollectionsKt___CollectionsKt.toLongArray(list);
            long zza = zzefVar.zza(longArray);
            zzeaVar = zzec.zzb;
            zzec zzecVar = new zzec(zza, 255L, zzeaVar);
            StringBuilder sb = new StringBuilder(str.length());
            for (int i = 0; i < str.length(); i++) {
                sb.append((char) UInt.constructor-impl(UInt.constructor-impl(str.charAt(i)) ^ UInt.constructor-impl((int) zzecVar.zza())));
            }
            return zzpf.zzg(zzfy.zzh().zzj(sb.toString()));
        } catch (Exception e) {
            throw new zzae(3, 18, e);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzee
    public final zzpf zza(zzpn zzpnVar) {
        zzfh zzb = zzfh.zzb();
        zzpf zzb2 = zzb(zzpnVar.zzi(), zzpnVar.zzj());
        zzb.zzf();
        long zza = zzb.zza(TimeUnit.MICROSECONDS);
        zzv zzvVar = zzv.zza;
        zzv.zza(zzx.zzm.zza(), zza);
        return zzb2;
    }
}
