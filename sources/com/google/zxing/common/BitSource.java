package com.google.zxing.common;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public final class BitSource {
    private int bitOffset;
    private int byteOffset;
    private final byte[] bytes;

    public BitSource(byte[] bArr) {
        this.bytes = bArr;
    }

    public int available() {
        return ((this.bytes.length - this.byteOffset) * 8) - this.bitOffset;
    }

    public int readBits(int i) {
        if (i < 1 || i > 32 || i > available()) {
            throw new IllegalArgumentException(String.valueOf(i));
        }
        int i2 = this.bitOffset;
        int i3 = 0;
        if (i2 > 0) {
            int i4 = 8 - i2;
            int min = Math.min(i, i4);
            int i5 = i4 - min;
            int i6 = (NotificationCenter.liveLocationsChanged >> (8 - min)) << i5;
            byte[] bArr = this.bytes;
            int i7 = this.byteOffset;
            int i8 = (i6 & bArr[i7]) >> i5;
            i -= min;
            int i9 = this.bitOffset + min;
            this.bitOffset = i9;
            if (i9 == 8) {
                this.bitOffset = 0;
                this.byteOffset = i7 + 1;
            }
            i3 = i8;
        }
        if (i <= 0) {
            return i3;
        }
        while (i >= 8) {
            int i10 = i3 << 8;
            byte[] bArr2 = this.bytes;
            int i11 = this.byteOffset;
            i3 = (bArr2[i11] & 255) | i10;
            this.byteOffset = i11 + 1;
            i -= 8;
        }
        if (i <= 0) {
            return i3;
        }
        int i12 = 8 - i;
        int i13 = (i3 << i) | ((((NotificationCenter.liveLocationsChanged >> i12) << i12) & this.bytes[this.byteOffset]) >> i12);
        this.bitOffset += i;
        return i13;
    }
}
