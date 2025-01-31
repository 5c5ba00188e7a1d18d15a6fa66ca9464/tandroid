package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzfu extends zzfx {
    final char[] zza;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    zzfu(String str, String str2) {
        super(r4, null);
        char[] cArr;
        zzft zzftVar = new zzft("base16()", "0123456789ABCDEF".toCharArray());
        this.zza = new char[512];
        cArr = zzftVar.zzf;
        zzff.zza(cArr.length == 16);
        for (int i = 0; i < 256; i++) {
            this.zza[i] = zzftVar.zza(i >>> 4);
            this.zza[i | 256] = zzftVar.zza(i & 15);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzfx, com.google.android.recaptcha.internal.zzfy
    final int zza(byte[] bArr, CharSequence charSequence) {
        if (charSequence.length() % 2 == 1) {
            throw new zzfw("Invalid input length " + charSequence.length());
        }
        int i = 0;
        int i2 = 0;
        while (i < charSequence.length()) {
            bArr[i2] = (byte) ((this.zzb.zzb(charSequence.charAt(i)) << 4) | this.zzb.zzb(charSequence.charAt(i + 1)));
            i += 2;
            i2++;
        }
        return i2;
    }

    @Override // com.google.android.recaptcha.internal.zzfx, com.google.android.recaptcha.internal.zzfy
    final void zzb(Appendable appendable, byte[] bArr, int i, int i2) {
        zzff.zzd(0, i2, bArr.length);
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = bArr[i3] & 255;
            appendable.append(this.zza[i4]);
            appendable.append(this.zza[i4 | 256]);
        }
    }
}
