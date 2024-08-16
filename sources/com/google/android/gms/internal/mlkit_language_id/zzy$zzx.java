package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzy$zzx extends zzeo<zzy$zzx, zza> implements zzgb {
    private static final zzy$zzx zzf;
    private static volatile zzgj<zzy$zzx> zzg;
    private int zzc;
    private zzy$zzam zzd;
    private int zze;

    private zzy$zzx() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb<zzy$zzx, zza> implements zzgb {
        private zza() {
            super(zzy$zzx.zzf);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzy$zzx>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzx();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဌ\u0001", new Object[]{"zzc", "zzd", "zze", zzai.zzb()});
            case 4:
                return zzf;
            case 5:
                zzgj<zzy$zzx> zzgjVar = zzg;
                zzgj<zzy$zzx> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzx.class) {
                        try {
                            zzgj<zzy$zzx> zzgjVar3 = zzg;
                            zzgj<zzy$zzx> zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzf);
                                zzg = zzaVar;
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
        zzy$zzx zzy_zzx = new zzy$zzx();
        zzf = zzy_zzx;
        zzeo.zza(zzy$zzx.class, zzy_zzx);
    }
}
