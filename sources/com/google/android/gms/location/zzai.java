package com.google.android.gms.location;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class zzai {
    public static String zza(int i) {
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    return "THROTTLE_NEVER";
                }
                throw new IllegalArgumentException();
            }
            return "THROTTLE_ALWAYS";
        }
        return "THROTTLE_BACKGROUND";
    }
}
