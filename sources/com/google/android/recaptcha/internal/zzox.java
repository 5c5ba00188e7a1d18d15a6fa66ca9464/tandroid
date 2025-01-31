package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzox extends zzit implements zzkf {
    private static final zzox zzb;
    private int zzd;

    static {
        zzox zzoxVar = new zzox();
        zzb = zzoxVar;
        zzit.zzD(zzox.class, zzoxVar);
    }

    private zzox() {
    }

    public static zzox zzg(byte[] bArr) {
        return (zzox) zzit.zzu(zzb, bArr);
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\f", new Object[]{"zzd"});
        }
        if (i2 == 3) {
            return new zzox();
        }
        zzor zzorVar = null;
        if (i2 == 4) {
            return new zzow(zzorVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }

    public final zzpb zzi() {
        zzpb zzb2 = zzpb.zzb(this.zzd);
        return zzb2 == null ? zzpb.zzk : zzb2;
    }
}
