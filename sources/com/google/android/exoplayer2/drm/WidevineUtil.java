package com.google.android.exoplayer2.drm;

import android.util.Pair;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class WidevineUtil {
    private static long getDurationRemainingSec(Map map, String str) {
        if (map != null) {
            try {
                String str2 = (String) map.get(str);
                if (str2 != null) {
                    return Long.parseLong(str2);
                }
                return -9223372036854775807L;
            } catch (NumberFormatException unused) {
                return -9223372036854775807L;
            }
        }
        return -9223372036854775807L;
    }

    public static Pair getLicenseDurationRemainingSec(DrmSession drmSession) {
        Map queryKeyStatus = drmSession.queryKeyStatus();
        if (queryKeyStatus == null) {
            return null;
        }
        return new Pair(Long.valueOf(getDurationRemainingSec(queryKeyStatus, "LicenseDurationRemaining")), Long.valueOf(getDurationRemainingSec(queryKeyStatus, "PlaybackDurationRemaining")));
    }
}
