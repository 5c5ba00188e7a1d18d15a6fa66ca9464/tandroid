package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public final class DeviceInfo implements Bundleable {
    private static final String FIELD_MAX_VOLUME;
    private static final String FIELD_MIN_VOLUME;
    private static final String FIELD_PLAYBACK_TYPE;
    public final int maxVolume;
    public final int minVolume;
    public final int playbackType;

    static {
        new DeviceInfo(0, 0, 0);
        FIELD_PLAYBACK_TYPE = Util.intToStringMaxRadix(0);
        FIELD_MIN_VOLUME = Util.intToStringMaxRadix(1);
        FIELD_MAX_VOLUME = Util.intToStringMaxRadix(2);
    }

    public DeviceInfo(int i, int i2, int i3) {
        this.playbackType = i;
        this.minVolume = i2;
        this.maxVolume = i3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DeviceInfo) {
            DeviceInfo deviceInfo = (DeviceInfo) obj;
            return this.playbackType == deviceInfo.playbackType && this.minVolume == deviceInfo.minVolume && this.maxVolume == deviceInfo.maxVolume;
        }
        return false;
    }

    public int hashCode() {
        return ((((527 + this.playbackType) * 31) + this.minVolume) * 31) + this.maxVolume;
    }

    @Override // com.google.android.exoplayer2.Bundleable
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(FIELD_PLAYBACK_TYPE, this.playbackType);
        bundle.putInt(FIELD_MIN_VOLUME, this.minVolume);
        bundle.putInt(FIELD_MAX_VOLUME, this.maxVolume);
        return bundle;
    }
}
