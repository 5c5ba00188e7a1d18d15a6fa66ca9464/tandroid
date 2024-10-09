package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public abstract class zzea extends zzdk {
    private static final Logger zzb = Logger.getLogger(zzea.class.getName());
    private static final boolean zzc = zzhn.zza();
    zzed zza;

    /* loaded from: classes.dex */
    private static class zza extends zzea {
        private final byte[] zzb;
        private final int zzc;
        private final int zzd;
        private int zze;

        zza(byte[] bArr, int i, int i2) {
            super();
            if (bArr == null) {
                throw new NullPointerException("buffer");
            }
            if (((bArr.length - i2) | i2) < 0) {
                throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", Integer.valueOf(bArr.length), 0, Integer.valueOf(i2)));
            }
            this.zzb = bArr;
            this.zzc = 0;
            this.zze = 0;
            this.zzd = i2;
        }

        private final void zzc(byte[] bArr, int i, int i2) {
            try {
                System.arraycopy(bArr, i, this.zzb, this.zze, i2);
                this.zze += i2;
            } catch (IndexOutOfBoundsException e) {
                throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), Integer.valueOf(i2)), e);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final int zza() {
            return this.zzd - this.zze;
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(byte b) {
            try {
                byte[] bArr = this.zzb;
                int i = this.zze;
                this.zze = i + 1;
                bArr[i] = b;
            } catch (IndexOutOfBoundsException e) {
                throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i) {
            if (i >= 0) {
                zzb(i);
            } else {
                zza(i);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, int i2) {
            zzb((i << 3) | i2);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, long j) {
            zza(i, 0);
            zza(j);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, zzdn zzdnVar) {
            zza(i, 2);
            zza(zzdnVar);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, zzfz zzfzVar) {
            zza(1, 3);
            zzc(2, i);
            zza(3, 2);
            zza(zzfzVar);
            zza(1, 4);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        final void zza(int i, zzfz zzfzVar, zzgp zzgpVar) {
            zza(i, 2);
            zzde zzdeVar = (zzde) zzfzVar;
            int zzg = zzdeVar.zzg();
            if (zzg == -1) {
                zzg = zzgpVar.zzd(zzdeVar);
                zzdeVar.zza(zzg);
            }
            zzb(zzg);
            zzgpVar.zza((Object) zzfzVar, this.zza);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, String str) {
            zza(i, 2);
            zza(str);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(int i, boolean z) {
            zza(i, 0);
            zza(z ? (byte) 1 : (byte) 0);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(long j) {
            if (zzea.zzc && zza() >= 10) {
                while ((j & (-128)) != 0) {
                    byte[] bArr = this.zzb;
                    int i = this.zze;
                    this.zze = i + 1;
                    zzhn.zza(bArr, i, (byte) ((((int) j) & NotificationCenter.dialogTranslate) | 128));
                    j >>>= 7;
                }
                byte[] bArr2 = this.zzb;
                int i2 = this.zze;
                this.zze = 1 + i2;
                zzhn.zza(bArr2, i2, (byte) j);
                return;
            }
            while ((j & (-128)) != 0) {
                try {
                    byte[] bArr3 = this.zzb;
                    int i3 = this.zze;
                    this.zze = i3 + 1;
                    bArr3[i3] = (byte) ((((int) j) & NotificationCenter.dialogTranslate) | 128);
                    j >>>= 7;
                } catch (IndexOutOfBoundsException e) {
                    throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
                }
            }
            byte[] bArr4 = this.zzb;
            int i4 = this.zze;
            this.zze = i4 + 1;
            bArr4[i4] = (byte) j;
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(zzdn zzdnVar) {
            zzb(zzdnVar.zza());
            zzdnVar.zza(this);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(zzfz zzfzVar) {
            zzb(zzfzVar.zzj());
            zzfzVar.zza(this);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zza(String str) {
            int i = this.zze;
            try {
                int zzg = zzea.zzg(str.length() * 3);
                int zzg2 = zzea.zzg(str.length());
                if (zzg2 != zzg) {
                    zzb(zzhp.zza(str));
                    this.zze = zzhp.zza(str, this.zzb, this.zze, zza());
                    return;
                }
                int i2 = i + zzg2;
                this.zze = i2;
                int zza = zzhp.zza(str, this.zzb, i2, zza());
                this.zze = i;
                zzb((zza - i) - zzg2);
                this.zze = zza;
            } catch (zzhq e) {
                this.zze = i;
                zza(str, e);
            } catch (IndexOutOfBoundsException e2) {
                throw new zzb(e2);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzdk
        public final void zza(byte[] bArr, int i, int i2) {
            zzc(bArr, i, i2);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzb(int i) {
            if (!zzea.zzc || zzdl.zza() || zza() < 5) {
                while ((i & (-128)) != 0) {
                    try {
                        byte[] bArr = this.zzb;
                        int i2 = this.zze;
                        this.zze = i2 + 1;
                        bArr[i2] = (byte) ((i & NotificationCenter.dialogTranslate) | 128);
                        i >>>= 7;
                    } catch (IndexOutOfBoundsException e) {
                        throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
                    }
                }
                byte[] bArr2 = this.zzb;
                int i3 = this.zze;
                this.zze = i3 + 1;
                bArr2[i3] = (byte) i;
                return;
            }
            if ((i & (-128)) == 0) {
                byte[] bArr3 = this.zzb;
                int i4 = this.zze;
                this.zze = 1 + i4;
                zzhn.zza(bArr3, i4, (byte) i);
                return;
            }
            byte[] bArr4 = this.zzb;
            int i5 = this.zze;
            this.zze = i5 + 1;
            zzhn.zza(bArr4, i5, (byte) (i | 128));
            int i6 = i >>> 7;
            if ((i6 & (-128)) == 0) {
                byte[] bArr5 = this.zzb;
                int i7 = this.zze;
                this.zze = 1 + i7;
                zzhn.zza(bArr5, i7, (byte) i6);
                return;
            }
            byte[] bArr6 = this.zzb;
            int i8 = this.zze;
            this.zze = i8 + 1;
            zzhn.zza(bArr6, i8, (byte) (i6 | 128));
            int i9 = i >>> 14;
            if ((i9 & (-128)) == 0) {
                byte[] bArr7 = this.zzb;
                int i10 = this.zze;
                this.zze = 1 + i10;
                zzhn.zza(bArr7, i10, (byte) i9);
                return;
            }
            byte[] bArr8 = this.zzb;
            int i11 = this.zze;
            this.zze = i11 + 1;
            zzhn.zza(bArr8, i11, (byte) (i9 | 128));
            int i12 = i >>> 21;
            if ((i12 & (-128)) == 0) {
                byte[] bArr9 = this.zzb;
                int i13 = this.zze;
                this.zze = 1 + i13;
                zzhn.zza(bArr9, i13, (byte) i12);
                return;
            }
            byte[] bArr10 = this.zzb;
            int i14 = this.zze;
            this.zze = i14 + 1;
            zzhn.zza(bArr10, i14, (byte) (i12 | 128));
            byte[] bArr11 = this.zzb;
            int i15 = this.zze;
            this.zze = 1 + i15;
            zzhn.zza(bArr11, i15, (byte) (i >>> 28));
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzb(int i, int i2) {
            zza(i, 0);
            zza(i2);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzb(int i, zzdn zzdnVar) {
            zza(1, 3);
            zzc(2, i);
            zza(3, zzdnVar);
            zza(1, 4);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzc(int i, int i2) {
            zza(i, 0);
            zzb(i2);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzc(int i, long j) {
            zza(i, 1);
            zzc(j);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzc(long j) {
            try {
                byte[] bArr = this.zzb;
                int i = this.zze;
                bArr[i] = (byte) j;
                bArr[i + 1] = (byte) (j >> 8);
                bArr[i + 2] = (byte) (j >> 16);
                bArr[i + 3] = (byte) (j >> 24);
                bArr[i + 4] = (byte) (j >> 32);
                bArr[i + 5] = (byte) (j >> 40);
                bArr[i + 6] = (byte) (j >> 48);
                this.zze = i + 8;
                bArr[i + 7] = (byte) (j >> 56);
            } catch (IndexOutOfBoundsException e) {
                throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zzd(int i) {
            try {
                byte[] bArr = this.zzb;
                int i2 = this.zze;
                bArr[i2] = (byte) i;
                bArr[i2 + 1] = (byte) (i >> 8);
                bArr[i2 + 2] = (byte) (i >> 16);
                this.zze = i2 + 4;
                bArr[i2 + 3] = (byte) (i >>> 24);
            } catch (IndexOutOfBoundsException e) {
                throw new zzb(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
            }
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzea
        public final void zze(int i, int i2) {
            zza(i, 5);
            zzd(i2);
        }
    }

    /* loaded from: classes.dex */
    public static class zzb extends IOException {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        zzb(String str, Throwable th) {
            super(r3.length() != 0 ? "CodedOutputStream was writing to a flat byte array and ran out of space.: ".concat(r3) : new String("CodedOutputStream was writing to a flat byte array and ran out of space.: "), th);
            String valueOf = String.valueOf(str);
        }

        zzb(Throwable th) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
        }
    }

    private zzea() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(zzfz zzfzVar, zzgp zzgpVar) {
        zzde zzdeVar = (zzde) zzfzVar;
        int zzg = zzdeVar.zzg();
        if (zzg == -1) {
            zzg = zzgpVar.zzd(zzdeVar);
            zzdeVar.zza(zzg);
        }
        return zzg(zzg) + zzg;
    }

    public static zzea zza(byte[] bArr) {
        return new zza(bArr, 0, bArr.length);
    }

    public static int zzb(double d) {
        return 8;
    }

    public static int zzb(float f) {
        return 4;
    }

    public static int zzb(int i, double d) {
        return zzg(i << 3) + 8;
    }

    public static int zzb(int i, float f) {
        return zzg(i << 3) + 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, zzfz zzfzVar, zzgp zzgpVar) {
        return zzg(i << 3) + zza(zzfzVar, zzgpVar);
    }

    public static int zzb(int i, String str) {
        return zzg(i << 3) + zzb(str);
    }

    public static int zzb(int i, boolean z) {
        return zzg(i << 3) + 1;
    }

    public static int zzb(zzdn zzdnVar) {
        int zza2 = zzdnVar.zza();
        return zzg(zza2) + zza2;
    }

    public static int zzb(zzfz zzfzVar) {
        int zzj = zzfzVar.zzj();
        return zzg(zzj) + zzj;
    }

    public static int zzb(String str) {
        int length;
        try {
            length = zzhp.zza(str);
        } catch (zzhq unused) {
            length = str.getBytes(zzeq.zza).length;
        }
        return zzg(length) + length;
    }

    public static int zzb(boolean z) {
        return 1;
    }

    public static int zzb(byte[] bArr) {
        int length = bArr.length;
        return zzg(length) + length;
    }

    public static int zzc(int i, zzdn zzdnVar) {
        int zzg = zzg(i << 3);
        int zza2 = zzdnVar.zza();
        return zzg + zzg(zza2) + zza2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, zzfz zzfzVar, zzgp zzgpVar) {
        int zzg = zzg(i << 3) << 1;
        zzde zzdeVar = (zzde) zzfzVar;
        int zzg2 = zzdeVar.zzg();
        if (zzg2 == -1) {
            zzg2 = zzgpVar.zzd(zzdeVar);
            zzdeVar.zza(zzg2);
        }
        return zzg + zzg2;
    }

    public static int zzc(zzfz zzfzVar) {
        return zzfzVar.zzj();
    }

    public static int zzd(int i, long j) {
        return zzg(i << 3) + zze(j);
    }

    public static int zzd(int i, zzdn zzdnVar) {
        return (zzg(8) << 1) + zzg(2, i) + zzc(3, zzdnVar);
    }

    public static int zzd(long j) {
        return zze(j);
    }

    public static int zze(int i) {
        return zzg(i << 3);
    }

    public static int zze(int i, long j) {
        return zzg(i << 3) + zze(j);
    }

    public static int zze(long j) {
        int i;
        if (((-128) & j) == 0) {
            return 1;
        }
        if (j < 0) {
            return 10;
        }
        if (((-34359738368L) & j) != 0) {
            j >>>= 28;
            i = 6;
        } else {
            i = 2;
        }
        if (((-2097152) & j) != 0) {
            i += 2;
            j >>>= 14;
        }
        return (j & (-16384)) != 0 ? i + 1 : i;
    }

    public static int zzf(int i) {
        if (i >= 0) {
            return zzg(i);
        }
        return 10;
    }

    public static int zzf(int i, int i2) {
        return zzg(i << 3) + zzf(i2);
    }

    public static int zzf(int i, long j) {
        return zzg(i << 3) + zze(zzi(j));
    }

    public static int zzf(long j) {
        return zze(zzi(j));
    }

    public static int zzg(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return (i & (-268435456)) == 0 ? 4 : 5;
    }

    public static int zzg(int i, int i2) {
        return zzg(i << 3) + zzg(i2);
    }

    public static int zzg(int i, long j) {
        return zzg(i << 3) + 8;
    }

    public static int zzg(long j) {
        return 8;
    }

    public static int zzh(int i) {
        return zzg(zzm(i));
    }

    public static int zzh(int i, int i2) {
        return zzg(i << 3) + zzg(zzm(i2));
    }

    public static int zzh(int i, long j) {
        return zzg(i << 3) + 8;
    }

    public static int zzh(long j) {
        return 8;
    }

    public static int zzi(int i) {
        return 4;
    }

    public static int zzi(int i, int i2) {
        return zzg(i << 3) + 4;
    }

    private static long zzi(long j) {
        return (j >> 63) ^ (j << 1);
    }

    public static int zzj(int i) {
        return 4;
    }

    public static int zzj(int i, int i2) {
        return zzg(i << 3) + 4;
    }

    public static int zzk(int i) {
        return zzf(i);
    }

    public static int zzk(int i, int i2) {
        return zzg(i << 3) + zzf(i2);
    }

    public static int zzl(int i) {
        return zzg(i);
    }

    private static int zzm(int i) {
        return (i >> 31) ^ (i << 1);
    }

    public abstract int zza();

    public abstract void zza(byte b);

    public final void zza(double d) {
        zzc(Double.doubleToRawLongBits(d));
    }

    public final void zza(float f) {
        zzd(Float.floatToRawIntBits(f));
    }

    public abstract void zza(int i);

    public final void zza(int i, double d) {
        zzc(i, Double.doubleToRawLongBits(d));
    }

    public final void zza(int i, float f) {
        zze(i, Float.floatToRawIntBits(f));
    }

    public abstract void zza(int i, int i2);

    public abstract void zza(int i, long j);

    public abstract void zza(int i, zzdn zzdnVar);

    public abstract void zza(int i, zzfz zzfzVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zza(int i, zzfz zzfzVar, zzgp zzgpVar);

    public abstract void zza(int i, String str);

    public abstract void zza(int i, boolean z);

    public abstract void zza(long j);

    public abstract void zza(zzdn zzdnVar);

    public abstract void zza(zzfz zzfzVar);

    public abstract void zza(String str);

    final void zza(String str, zzhq zzhqVar) {
        zzb.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", (Throwable) zzhqVar);
        byte[] bytes = str.getBytes(zzeq.zza);
        try {
            zzb(bytes.length);
            zza(bytes, 0, bytes.length);
        } catch (zzb e) {
            throw e;
        } catch (IndexOutOfBoundsException e2) {
            throw new zzb(e2);
        }
    }

    public final void zza(boolean z) {
        zza(z ? (byte) 1 : (byte) 0);
    }

    public final void zzb() {
        if (zza() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public abstract void zzb(int i);

    public abstract void zzb(int i, int i2);

    public final void zzb(int i, long j) {
        zza(i, zzi(j));
    }

    public abstract void zzb(int i, zzdn zzdnVar);

    public final void zzb(long j) {
        zza(zzi(j));
    }

    public final void zzc(int i) {
        zzb(zzm(i));
    }

    public abstract void zzc(int i, int i2);

    public abstract void zzc(int i, long j);

    public abstract void zzc(long j);

    public abstract void zzd(int i);

    public final void zzd(int i, int i2) {
        zzc(i, zzm(i2));
    }

    public abstract void zze(int i, int i2);
}
