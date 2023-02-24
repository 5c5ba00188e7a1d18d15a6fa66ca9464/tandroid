package com.google.android.exoplayer2.video;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class ColorInfo implements Bundleable {
    public static final Bundleable.Creator<ColorInfo> CREATOR;
    private static final String FIELD_COLOR_RANGE;
    private static final String FIELD_COLOR_SPACE;
    private static final String FIELD_COLOR_TRANSFER;
    private static final String FIELD_HDR_STATIC_INFO;
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    private int hashCode;
    public final byte[] hdrStaticInfo;

    public static int isoColorPrimariesToColorSpace(int i) {
        if (i != 1) {
            if (i != 9) {
                return (i == 4 || i == 5 || i == 6 || i == 7) ? 2 : -1;
            }
            return 6;
        }
        return 1;
    }

    public static int isoTransferCharacteristicsToColorTransfer(int i) {
        if (i != 1) {
            if (i != 16) {
                if (i != 18) {
                    return (i == 6 || i == 7) ? 3 : -1;
                }
                return 7;
            }
            return 6;
        }
        return 3;
    }

    static {
        new ColorInfo(1, 2, 3, null);
        FIELD_COLOR_SPACE = Util.intToStringMaxRadix(0);
        FIELD_COLOR_RANGE = Util.intToStringMaxRadix(1);
        FIELD_COLOR_TRANSFER = Util.intToStringMaxRadix(2);
        FIELD_HDR_STATIC_INFO = Util.intToStringMaxRadix(3);
        CREATOR = ColorInfo$$ExternalSyntheticLambda0.INSTANCE;
    }

    public ColorInfo(int i, int i2, int i3, byte[] bArr) {
        this.colorSpace = i;
        this.colorRange = i2;
        this.colorTransfer = i3;
        this.hdrStaticInfo = bArr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ColorInfo.class != obj.getClass()) {
            return false;
        }
        ColorInfo colorInfo = (ColorInfo) obj;
        return this.colorSpace == colorInfo.colorSpace && this.colorRange == colorInfo.colorRange && this.colorTransfer == colorInfo.colorTransfer && Arrays.equals(this.hdrStaticInfo, colorInfo.hdrStaticInfo);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorInfo(");
        sb.append(this.colorSpace);
        sb.append(", ");
        sb.append(this.colorRange);
        sb.append(", ");
        sb.append(this.colorTransfer);
        sb.append(", ");
        sb.append(this.hdrStaticInfo != null);
        sb.append(")");
        return sb.toString();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((((((527 + this.colorSpace) * 31) + this.colorRange) * 31) + this.colorTransfer) * 31) + Arrays.hashCode(this.hdrStaticInfo);
        }
        return this.hashCode;
    }

    @Override // com.google.android.exoplayer2.Bundleable
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(FIELD_COLOR_SPACE, this.colorSpace);
        bundle.putInt(FIELD_COLOR_RANGE, this.colorRange);
        bundle.putInt(FIELD_COLOR_TRANSFER, this.colorTransfer);
        bundle.putByteArray(FIELD_HDR_STATIC_INFO, this.hdrStaticInfo);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ColorInfo lambda$static$0(Bundle bundle) {
        return new ColorInfo(bundle.getInt(FIELD_COLOR_SPACE, -1), bundle.getInt(FIELD_COLOR_RANGE, -1), bundle.getInt(FIELD_COLOR_TRANSFER, -1), bundle.getByteArray(FIELD_HDR_STATIC_INFO));
    }
}
