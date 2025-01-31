package com.google.android.recaptcha.internal;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class zzfv extends zzfx {
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    zzfv(String str, String str2, Character ch) {
        super(r0, ch);
        char[] cArr;
        zzft zzftVar = new zzft(str, str2.toCharArray());
        cArr = zzftVar.zzf;
        zzff.zza(cArr.length == 64);
    }

    @Override // com.google.android.recaptcha.internal.zzfx, com.google.android.recaptcha.internal.zzfy
    final int zza(byte[] bArr, CharSequence charSequence) {
        CharSequence zze = zze(charSequence);
        if (!this.zzb.zzc(zze.length())) {
            throw new zzfw("Invalid input length " + zze.length());
        }
        int i = 0;
        int i2 = 0;
        while (i < zze.length()) {
            int i3 = i2 + 1;
            int zzb = (this.zzb.zzb(zze.charAt(i)) << 18) | (this.zzb.zzb(zze.charAt(i + 1)) << 12);
            bArr[i2] = (byte) (zzb >>> 16);
            int i4 = i + 2;
            if (i4 < zze.length()) {
                int i5 = i + 3;
                int zzb2 = zzb | (this.zzb.zzb(zze.charAt(i4)) << 6);
                int i6 = i2 + 2;
                bArr[i3] = (byte) ((zzb2 >>> 8) & NotificationCenter.liveLocationsChanged);
                if (i5 < zze.length()) {
                    i += 4;
                    i2 += 3;
                    bArr[i6] = (byte) ((zzb2 | this.zzb.zzb(zze.charAt(i5))) & NotificationCenter.liveLocationsChanged);
                } else {
                    i2 = i6;
                    i = i5;
                }
            } else {
                i = i4;
                i2 = i3;
            }
        }
        return i2;
    }

    @Override // com.google.android.recaptcha.internal.zzfx, com.google.android.recaptcha.internal.zzfy
    final void zzb(Appendable appendable, byte[] bArr, int i, int i2) {
        int i3 = 0;
        zzff.zzd(0, i2, bArr.length);
        for (int i4 = i2; i4 >= 3; i4 -= 3) {
            int i5 = bArr[i3] & 255;
            int i6 = ((bArr[i3 + 1] & 255) << 8) | (i5 << 16) | (bArr[i3 + 2] & 255);
            appendable.append(this.zzb.zza(i6 >>> 18));
            appendable.append(this.zzb.zza((i6 >>> 12) & 63));
            appendable.append(this.zzb.zza((i6 >>> 6) & 63));
            appendable.append(this.zzb.zza(i6 & 63));
            i3 += 3;
        }
        if (i3 < i2) {
            zzf(appendable, bArr, i3, i2 - i3);
        }
    }
}
