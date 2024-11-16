package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzbh extends zzeo implements zzgb {
    private static final zzy$zzbh zzo;
    private static volatile zzgj zzp;
    private int zzc;
    private String zzd = "";
    private String zze = "";
    private String zzf = "";
    private String zzg = "";
    private String zzh = "";
    private String zzi = "";
    private String zzj = "";
    private zzew zzk = zzeo.zzl();
    private String zzl = "";
    private boolean zzm;
    private boolean zzn;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzbh.zzo);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }

        public final zza zza(Iterable iterable) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zza(iterable);
            return this;
        }

        public final zza zza(String str) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zza(str);
            return this;
        }

        public final zza zza(boolean z) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zza(true);
            return this;
        }

        public final zza zzb(String str) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zzb(str);
            return this;
        }

        public final zza zzb(boolean z) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zzb(true);
            return this;
        }

        public final zza zzc(String str) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zzc(str);
            return this;
        }

        public final zza zzd(String str) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zzd(str);
            return this;
        }

        public final zza zze(String str) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzbh) this.zza).zze(str);
            return this;
        }
    }

    static {
        zzy$zzbh zzy_zzbh = new zzy$zzbh();
        zzo = zzy_zzbh;
        zzeo.zza(zzy$zzbh.class, zzy_zzbh);
    }

    private zzy$zzbh() {
    }

    public static zza zza(zzy$zzbh zzy_zzbh) {
        return (zza) zzo.zza((zzeo) zzy_zzbh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(Iterable iterable) {
        zzew zzewVar = this.zzk;
        if (!zzewVar.zza()) {
            this.zzk = zzeo.zza(zzewVar);
        }
        zzde.zza(iterable, this.zzk);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(String str) {
        str.getClass();
        this.zzc |= 1;
        this.zzd = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(boolean z) {
        this.zzc |= 256;
        this.zzm = true;
    }

    public static zza zzb() {
        return (zza) zzo.zzh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzb(String str) {
        str.getClass();
        this.zzc |= 2;
        this.zze = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzb(boolean z) {
        this.zzc |= 512;
        this.zzn = true;
    }

    public static zzy$zzbh zzc() {
        return zzo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzc(String str) {
        str.getClass();
        this.zzc |= 8;
        this.zzg = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzd(String str) {
        str.getClass();
        this.zzc |= 16;
        this.zzh = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zze(String str) {
        str.getClass();
        this.zzc |= 128;
        this.zzl = str;
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbh();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzo, "\u0001\u000b\u0000\u0001\u0001\u000b\u000b\u0000\u0001\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဈ\u0002\u0004ဈ\u0003\u0005ဈ\u0004\u0006ဈ\u0005\u0007ဈ\u0006\b\u001a\tဈ\u0007\nဇ\b\u000bဇ\t", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", "zzl", "zzm", "zzn"});
            case 4:
                return zzo;
            case 5:
                zzgj zzgjVar = zzp;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzbh.class) {
                        try {
                            zzgj zzgjVar3 = zzp;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzo);
                                zzp = zzaVar;
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

    public final String zza() {
        return this.zzh;
    }
}
