package com.google.android.gms.internal.play_billing;
/* loaded from: classes.dex */
public final class zzfb extends zzcb implements zzdg {
    private static final zzfb zzb;
    private int zzd;
    private int zze = 0;
    private Object zzf;
    private int zzg;
    private zzfj zzh;
    private zzfs zzi;

    static {
        zzfb zzfbVar = new zzfb();
        zzb = zzfbVar;
        zzcb.zzp(zzfb.class, zzfbVar);
    }

    private zzfb() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzA(zzfb zzfbVar, int i) {
        zzfbVar.zzg = i - 1;
        zzfbVar.zzd |= 1;
    }

    public static zzfa zzv() {
        return (zzfa) zzb.zzg();
    }

    public static zzfb zzx(byte[] bArr, zzbn zzbnVar) {
        return (zzfb) zzcb.zzj(zzb, bArr, zzbnVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzy(zzfb zzfbVar, zzfj zzfjVar) {
        zzfjVar.getClass();
        zzfbVar.zzh = zzfjVar;
        zzfbVar.zzd |= 2;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcb
    protected final Object zzu(int i, Object obj, Object obj2) {
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
                    return new zzfa(null);
                }
                return new zzfb();
            }
            return zzcb.zzm(zzb, "\u0001\u0004\u0001\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001᠌\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004<\u0000", new Object[]{"zzf", "zze", "zzd", "zzg", zzfc.zza, "zzh", "zzi", zzfw.class});
        }
        return (byte) 1;
    }
}
