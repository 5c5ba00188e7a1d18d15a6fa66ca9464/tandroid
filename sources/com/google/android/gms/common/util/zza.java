package com.google.android.gms.common.util;

import org.telegram.messenger.MediaDataController;

/* loaded from: classes.dex */
public abstract class zza {
    public static int zza(int i) {
        if (i == -1) {
            return -1;
        }
        return i / MediaDataController.MAX_STYLE_RUNS_COUNT;
    }
}
