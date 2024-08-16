package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzy$zzw extends zzeo<zzy$zzw, zza> implements zzgb {
    private static final zzy$zzw zzg;
    private static volatile zzgj<zzy$zzw> zzh;
    private int zzc;
    private zzy$zzaf zzd;
    private zzy$zzn zze;
    private zzy$zzae zzf;

    private zzy$zzw() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb<zzy$zzw, zza> implements zzgb {
        private zza() {
            super(zzy$zzw.zzg);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzy$zzw>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzw();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002", new Object[]{"zzc", "zzd", "zze", "zzf"});
            case 4:
                return zzg;
            case 5:
                zzgj<zzy$zzw> zzgjVar = zzh;
                zzgj<zzy$zzw> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzw.class) {
                        try {
                            zzgj<zzy$zzw> zzgjVar3 = zzh;
                            zzgj<zzy$zzw> zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzg);
                                zzh = zzaVar;
                                zzgjVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzgjVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    static {
        zzy$zzw zzy_zzw = new zzy$zzw();
        zzg = zzy_zzw;
        zzeo.zza(zzy$zzw.class, zzy_zzw);
    }
}
