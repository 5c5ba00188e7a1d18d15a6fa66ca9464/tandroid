package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzk extends zzeo implements zzgb {
    private static final zzid$zzk zzf;
    private static volatile zzgj zzg;
    private int zzc;
    private int zzd;
    private float zze;

    /* loaded from: classes.dex */
    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);

        private static final zzes zze = new zziq();
        private final int zzf;

        zza(int i) {
            this.zzf = i;
        }

        public static zzev zzb() {
            return zzip.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzf;
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzid$zzk.zzf);
        }

        /* synthetic */ zzb(zzic zzicVar) {
            this();
        }
    }

    static {
        zzid$zzk zzid_zzk = new zzid$zzk();
        zzf = zzid_zzk;
        zzeo.zza(zzid$zzk.class, zzid_zzk);
    }

    private zzid$zzk() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v15, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzk();
            case 2:
                return new zzb(zzicVar);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဌ\u0000\u0002ခ\u0001", new Object[]{"zzc", "zzd", zza.zzb(), "zze"});
            case 4:
                return zzf;
            case 5:
                zzgj zzgjVar = zzg;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzk.class) {
                        try {
                            zzgj zzgjVar3 = zzg;
                            zzgj zzgjVar4 = zzgjVar3;
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
}
