package com.google.android.gms.location;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class zzai {
    public static String zza(int i) {
        if (i != 0) {
            if (i == 1) {
                return "THROTTLE_ALWAYS";
            }
            if (i != 2) {
                throw new IllegalArgumentException();
            }
            return "THROTTLE_NEVER";
        }
        return "THROTTLE_BACKGROUND";
    }
}
