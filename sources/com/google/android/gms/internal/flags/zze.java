package com.google.android.gms.internal.flags;

import android.os.StrictMode;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public abstract class zze {
    public static Object zza(Callable callable) {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
            return callable.call();
        } finally {
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }
}
