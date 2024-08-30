package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* loaded from: classes.dex */
public final class zzy$zzai extends zzeo implements zzgb {
    private static final zzy$zzai zzg;
    private static volatile zzgj zzh;
    private int zzc;
    private float zzd;
    private float zze;
    private float zzf;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzai.zzg);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }

        public final zza zza(float f) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzai) this.zza).zza(f);
            return this;
        }
    }

    static {
        zzy$zzai zzy_zzai = new zzy$zzai();
        zzg = zzy_zzai;
        zzeo.zza(zzy$zzai.class, zzy_zzai);
    }

    private zzy$zzai() {
    }

    public static zza zza() {
        return (zza) zzg.zzh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(float f) {
        this.zzc |= 4;
        this.zzf = f;
    }

    public static zzy$zzai zzb() {
        return zzg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzai();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ခ\u0000\u0002ခ\u0001\u0003ခ\u0002", new Object[]{"zzc", "zzd", "zze", "zzf"});
            case 4:
                return zzg;
            case 5:
                zzgj zzgjVar = zzh;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzai.class) {
                        try {
                            zzgj zzgjVar3 = zzh;
                            zzgj zzgjVar4 = zzgjVar3;
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
}
