package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zza extends zzjb implements zzkm {
    private static final zzfi$zza zzf;
    private static volatile zzkx zzg;
    private int zzc;
    private String zzd = "";
    private String zze = "";

    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zza.zzf);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }

        public final zza zza(String str) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zza) this.zza).zza(str);
            return this;
        }

        public final zza zzb(String str) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zza) this.zza).zzb(str);
            return this;
        }
    }

    static {
        zzfi$zza zzfi_zza = new zzfi$zza();
        zzf = zzfi_zza;
        zzjb.zza(zzfi$zza.class, zzfi_zza);
    }

    private zzfi$zza() {
    }

    public static zza zza() {
        return (zza) zzf.zzj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(String str) {
        str.getClass();
        this.zzc |= 1;
        this.zzd = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzb(String str) {
        str.getClass();
        this.zzc |= 2;
        this.zze = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zza();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001", new Object[]{"zzc", "zzd", "zze"});
            case 4:
                return zzf;
            case 5:
                zzkx zzkxVar = zzg;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zza.class) {
                        try {
                            zzkx zzkxVar3 = zzg;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzf);
                                zzg = zzaVar;
                                zzkxVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzkxVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
