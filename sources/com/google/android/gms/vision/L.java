package com.google.android.gms.vision;

import android.util.Log;

/* loaded from: classes.dex */
public abstract class L {
    public static int d(String str, Object... objArr) {
        if (Log.isLoggable("Vision", 3)) {
            return Log.d("Vision", String.format(str, objArr));
        }
        return 0;
    }

    public static int e(String str, Object... objArr) {
        if (Log.isLoggable("Vision", 6)) {
            return Log.e("Vision", String.format(str, objArr));
        }
        return 0;
    }

    public static int e(Throwable th, String str, Object... objArr) {
        if (!Log.isLoggable("Vision", 6)) {
            return 0;
        }
        boolean isLoggable = Log.isLoggable("Vision", 3);
        String format = String.format(str, objArr);
        if (isLoggable) {
            return Log.e("Vision", format, th);
        }
        String valueOf = String.valueOf(th);
        StringBuilder sb = new StringBuilder(format.length() + 2 + valueOf.length());
        sb.append(format);
        sb.append(": ");
        sb.append(valueOf);
        return Log.e("Vision", sb.toString());
    }

    public static int i(String str, Object... objArr) {
        if (Log.isLoggable("Vision", 4)) {
            return Log.i("Vision", String.format(str, objArr));
        }
        return 0;
    }

    public static int v(String str, Object... objArr) {
        if (Log.isLoggable("Vision", 2)) {
            return Log.v("Vision", String.format(str, objArr));
        }
        return 0;
    }
}
