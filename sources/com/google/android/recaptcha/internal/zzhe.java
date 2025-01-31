package com.google.android.recaptcha.internal;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class zzhe extends zzhh {
    private final byte[] zzc;
    private final int zzd;
    private int zze;

    zzhe(byte[] bArr, int i, int i2) {
        super(null);
        int length = bArr.length;
        if (((length - i2) | i2) < 0) {
            throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", Integer.valueOf(length), 0, Integer.valueOf(i2)));
        }
        this.zzc = bArr;
        this.zze = 0;
        this.zzd = i2;
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final int zza() {
        return this.zzd - this.zze;
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzb(byte b) {
        try {
            byte[] bArr = this.zzc;
            int i = this.zze;
            this.zze = i + 1;
            bArr[i] = b;
        } catch (IndexOutOfBoundsException e) {
            throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
        }
    }

    public final void zzc(byte[] bArr, int i, int i2) {
        try {
            System.arraycopy(bArr, 0, this.zzc, this.zze, i2);
            this.zze += i2;
        } catch (IndexOutOfBoundsException e) {
            throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), Integer.valueOf(i2)), e);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzd(int i, boolean z) {
        zzq(i << 3);
        zzb(z ? (byte) 1 : (byte) 0);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zze(int i, zzgw zzgwVar) {
        zzq((i << 3) | 2);
        zzq(zzgwVar.zzd());
        zzgwVar.zzi(this);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzf(int i, int i2) {
        zzq((i << 3) | 5);
        zzg(i2);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzg(int i) {
        try {
            byte[] bArr = this.zzc;
            int i2 = this.zze;
            bArr[i2] = (byte) (i & NotificationCenter.liveLocationsChanged);
            bArr[i2 + 1] = (byte) ((i >> 8) & NotificationCenter.liveLocationsChanged);
            bArr[i2 + 2] = (byte) ((i >> 16) & NotificationCenter.liveLocationsChanged);
            this.zze = i2 + 4;
            bArr[i2 + 3] = (byte) ((i >> 24) & NotificationCenter.liveLocationsChanged);
        } catch (IndexOutOfBoundsException e) {
            throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzh(int i, long j) {
        zzq((i << 3) | 1);
        zzi(j);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzi(long j) {
        try {
            byte[] bArr = this.zzc;
            int i = this.zze;
            bArr[i] = (byte) (((int) j) & NotificationCenter.liveLocationsChanged);
            bArr[i + 1] = (byte) (((int) (j >> 8)) & NotificationCenter.liveLocationsChanged);
            bArr[i + 2] = (byte) (((int) (j >> 16)) & NotificationCenter.liveLocationsChanged);
            bArr[i + 3] = (byte) (((int) (j >> 24)) & NotificationCenter.liveLocationsChanged);
            bArr[i + 4] = (byte) (((int) (j >> 32)) & NotificationCenter.liveLocationsChanged);
            bArr[i + 5] = (byte) (((int) (j >> 40)) & NotificationCenter.liveLocationsChanged);
            bArr[i + 6] = (byte) (((int) (j >> 48)) & NotificationCenter.liveLocationsChanged);
            this.zze = i + 8;
            bArr[i + 7] = (byte) (((int) (j >> 56)) & NotificationCenter.liveLocationsChanged);
        } catch (IndexOutOfBoundsException e) {
            throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzj(int i, int i2) {
        zzq(i << 3);
        zzk(i2);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzk(int i) {
        if (i >= 0) {
            zzq(i);
        } else {
            zzs(i);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzl(byte[] bArr, int i, int i2) {
        zzc(bArr, 0, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzm(int i, String str) {
        zzq((i << 3) | 2);
        zzn(str);
    }

    public final void zzn(String str) {
        int i = this.zze;
        try {
            int zzy = zzhh.zzy(str.length() * 3);
            int zzy2 = zzhh.zzy(str.length());
            if (zzy2 != zzy) {
                zzq(zzma.zzc(str));
                byte[] bArr = this.zzc;
                int i2 = this.zze;
                this.zze = zzma.zzb(str, bArr, i2, this.zzd - i2);
                return;
            }
            int i3 = i + zzy2;
            this.zze = i3;
            int zzb = zzma.zzb(str, this.zzc, i3, this.zzd - i3);
            this.zze = i;
            zzq((zzb - i) - zzy2);
            this.zze = zzb;
        } catch (zzlz e) {
            this.zze = i;
            zzC(str, e);
        } catch (IndexOutOfBoundsException e2) {
            throw new zzhf(e2);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzo(int i, int i2) {
        zzq((i << 3) | i2);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzp(int i, int i2) {
        zzq(i << 3);
        zzq(i2);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzq(int i) {
        while ((i & (-128)) != 0) {
            try {
                byte[] bArr = this.zzc;
                int i2 = this.zze;
                this.zze = i2 + 1;
                bArr[i2] = (byte) ((i & NotificationCenter.dialogTranslate) | 128);
                i >>>= 7;
            } catch (IndexOutOfBoundsException e) {
                throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
            }
        }
        byte[] bArr2 = this.zzc;
        int i3 = this.zze;
        this.zze = i3 + 1;
        bArr2[i3] = (byte) i;
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzr(int i, long j) {
        zzq(i << 3);
        zzs(j);
    }

    @Override // com.google.android.recaptcha.internal.zzhh
    public final void zzs(long j) {
        boolean z;
        z = zzhh.zzd;
        if (!z || this.zzd - this.zze < 10) {
            while ((j & (-128)) != 0) {
                try {
                    byte[] bArr = this.zzc;
                    int i = this.zze;
                    this.zze = i + 1;
                    bArr[i] = (byte) ((((int) j) & NotificationCenter.dialogTranslate) | 128);
                    j >>>= 7;
                } catch (IndexOutOfBoundsException e) {
                    throw new zzhf(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.zze), Integer.valueOf(this.zzd), 1), e);
                }
            }
            byte[] bArr2 = this.zzc;
            int i2 = this.zze;
            this.zze = i2 + 1;
            bArr2[i2] = (byte) j;
            return;
        }
        while (true) {
            int i3 = (int) j;
            if ((j & (-128)) == 0) {
                byte[] bArr3 = this.zzc;
                int i4 = this.zze;
                this.zze = 1 + i4;
                zzlv.zzn(bArr3, i4, (byte) i3);
                return;
            }
            byte[] bArr4 = this.zzc;
            int i5 = this.zze;
            this.zze = i5 + 1;
            zzlv.zzn(bArr4, i5, (byte) ((i3 & NotificationCenter.dialogTranslate) | 128));
            j >>>= 7;
        }
    }
}
