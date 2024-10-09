package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class DeviceInfo implements Bundleable {
    public final int maxVolume;
    public final int minVolume;
    public final int playbackType;
    public static final DeviceInfo UNKNOWN = new DeviceInfo(0, 0, 0);
    private static final String FIELD_PLAYBACK_TYPE = Util.intToStringMaxRadix(0);
    private static final String FIELD_MIN_VOLUME = Util.intToStringMaxRadix(1);
    private static final String FIELD_MAX_VOLUME = Util.intToStringMaxRadix(2);
    public static final Bundleable.Creator CREATOR = new Bundleable.Creator() { // from class: com.google.android.exoplayer2.DeviceInfo$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.Bundleable.Creator
        public final Bundleable fromBundle(Bundle bundle) {
            DeviceInfo lambda$static$0;
            lambda$static$0 = DeviceInfo.lambda$static$0(bundle);
            return lambda$static$0;
        }
    };

    public DeviceInfo(int i, int i2, int i3) {
        this.playbackType = i;
        this.minVolume = i2;
        this.maxVolume = i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ DeviceInfo lambda$static$0(Bundle bundle) {
        return new DeviceInfo(bundle.getInt(FIELD_PLAYBACK_TYPE, 0), bundle.getInt(FIELD_MIN_VOLUME, 0), bundle.getInt(FIELD_MAX_VOLUME, 0));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DeviceInfo)) {
            return false;
        }
        DeviceInfo deviceInfo = (DeviceInfo) obj;
        return this.playbackType == deviceInfo.playbackType && this.minVolume == deviceInfo.minVolume && this.maxVolume == deviceInfo.maxVolume;
    }

    public int hashCode() {
        return ((((this.playbackType + 527) * 31) + this.minVolume) * 31) + this.maxVolume;
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
