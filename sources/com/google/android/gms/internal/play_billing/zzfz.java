package com.google.android.gms.internal.play_billing;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzfz extends zzcb implements zzdg {
    private static final zzfz zzb;
    private int zzd;
    private int zze = 0;
    private Object zzf;
    private zzfm zzg;

    static {
        zzfz zzfzVar = new zzfz();
        zzb = zzfzVar;
        zzcb.zzp(zzfz.class, zzfzVar);
    }

    private zzfz() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzA(zzfz zzfzVar, zzff zzffVar) {
        zzffVar.getClass();
        zzfzVar.zzf = zzffVar;
        zzfzVar.zze = 3;
    }

    public static zzfy zzv() {
        return (zzfy) zzb.zzg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzx(zzfz zzfzVar, zzgd zzgdVar) {
        zzgdVar.getClass();
        zzfzVar.zzf = zzgdVar;
        zzfzVar.zze = 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzy(zzfz zzfzVar, zzfm zzfmVar) {
        zzfzVar.zzg = zzfmVar;
        zzfzVar.zzd |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzz(zzfz zzfzVar, zzfb zzfbVar) {
        zzfbVar.getClass();
        zzfzVar.zzf = zzfbVar;
        zzfzVar.zze = 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.play_billing.zzcb
    public final Object zzu(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 != 0) {
            if (i2 != 2) {
                if (i2 != 3) {
                    if (i2 != 4) {
                        if (i2 != 5) {
                            return null;
                        }
                        return zzb;
                    }
                    return new zzfy(null);
                }
                return new zzfz();
            }
            return zzcb.zzm(zzb, "\u0001\u0004\u0001\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဉ\u0000\u0002<\u0000\u0003<\u0000\u0004<\u0000", new Object[]{"zzf", "zze", "zzd", "zzg", zzfb.class, zzff.class, zzgd.class});
        }
        return (byte) 1;
    }
}
