package com.google.android.gms.internal.clearcut;

import com.google.android.gms.internal.clearcut.zzcg;
/* loaded from: classes.dex */
public final class zzge$zzd extends zzcg<zzge$zzd, zza> implements zzdq {
    private static volatile zzdz<zzge$zzd> zzbg;
    private static final zzge$zzd zztx;
    private int zzbb;
    private int zztu;
    private String zztv = "";
    private String zztw = "";

    /* loaded from: classes.dex */
    public static final class zza extends zzcg.zza<zzge$zzd, zza> implements zzdq {
        private zza() {
            super(zzge$zzd.zztx);
        }

        /* synthetic */ zza(zzgf zzgfVar) {
            this();
        }
    }

    static {
        zzge$zzd zzge_zzd = new zzge$zzd();
        zztx = zzge_zzd;
        zzcg.zza(zzge$zzd.class, zzge_zzd);
    }

    private zzge$zzd() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.clearcut.zzdz<com.google.android.gms.internal.clearcut.zzge$zzd>, com.google.android.gms.internal.clearcut.zzcg$zzb] */
    @Override // com.google.android.gms.internal.clearcut.zzcg
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzgf.zzba[i - 1]) {
            case 1:
                return new zzge$zzd();
            case 2:
                return new zza(null);
            case 3:
                return zzcg.zza(zztx, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0004\u0000\u0000\u0000\u0001\u0004\u0000\u0002\b\u0001\u0003\b\u0002", new Object[]{"zzbb", "zztu", "zztv", "zztw"});
            case 4:
                return zztx;
            case 5:
                zzdz<zzge$zzd> zzdzVar = zzbg;
                zzdz<zzge$zzd> zzdzVar2 = zzdzVar;
                if (zzdzVar == null) {
                    synchronized (zzge$zzd.class) {
                        try {
                            zzdz<zzge$zzd> zzdzVar3 = zzbg;
                            zzdz<zzge$zzd> zzdzVar4 = zzdzVar3;
                            if (zzdzVar3 == null) {
                                ?? zzbVar = new zzcg.zzb(zztx);
                                zzbg = zzbVar;
                                zzdzVar4 = zzbVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzdzVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
