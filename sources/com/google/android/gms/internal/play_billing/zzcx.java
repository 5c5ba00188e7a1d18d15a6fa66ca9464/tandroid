package com.google.android.gms.internal.play_billing;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzcx implements zzdq {
    private static final zzdd zza = new zzcv();
    private final zzdd zzb;

    public zzcx() {
        zzdd zzddVar;
        zzbw zza2 = zzbw.zza();
        try {
            zzddVar = (zzdd) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", null).invoke(null, null);
        } catch (Exception unused) {
            zzddVar = zza;
        }
        zzcw zzcwVar = new zzcw(zza2, zzddVar);
        byte[] bArr = zzcg.zzd;
        this.zzb = zzcwVar;
    }

    private static boolean zzb(zzdc zzdcVar) {
        return zzdcVar.zzc() + (-1) != 1;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdq
    public final zzdp zza(Class cls) {
        zzdk zza2;
        zzct zzc;
        zzeg zzy;
        zzbo zza3;
        zzda zza4;
        zzeg zzy2;
        zzbo zza5;
        zzdr.zzC(cls);
        zzdc zzb = this.zzb.zzb(cls);
        if (zzb.zzb()) {
            if (zzcb.class.isAssignableFrom(cls)) {
                zzy2 = zzdr.zzz();
                zza5 = zzbq.zzb();
            } else {
                zzy2 = zzdr.zzy();
                zza5 = zzbq.zza();
            }
            return zzdj.zzc(zzy2, zza5, zzb.zza());
        }
        if (zzcb.class.isAssignableFrom(cls)) {
            boolean zzb2 = zzb(zzb);
            zza2 = zzdl.zzb();
            zzc = zzct.zzd();
            zzy = zzdr.zzz();
            zza3 = zzb2 ? zzbq.zzb() : null;
            zza4 = zzdb.zzb();
        } else {
            boolean zzb3 = zzb(zzb);
            zza2 = zzdl.zza();
            zzc = zzct.zzc();
            zzy = zzdr.zzy();
            zza3 = zzb3 ? zzbq.zza() : null;
            zza4 = zzdb.zza();
        }
        return zzdi.zzl(cls, zzb, zza2, zzc, zzy, zza3, zza4);
    }
}
