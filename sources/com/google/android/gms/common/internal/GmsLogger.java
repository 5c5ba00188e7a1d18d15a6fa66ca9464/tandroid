package com.google.android.gms.common.internal;

import android.util.Log;

/* loaded from: classes.dex */
public final class GmsLogger {
    private final String zza;
    private final String zzb;

    public GmsLogger(String str) {
        this(str, null);
    }

    public GmsLogger(String str, String str2) {
        Preconditions.checkNotNull(str, "log tag cannot be null");
        Preconditions.checkArgument(str.length() <= 23, "tag \"%s\" is longer than the %d character maximum", str, 23);
        this.zza = str;
        if (str2 == null || str2.length() <= 0) {
            this.zzb = null;
        } else {
            this.zzb = str2;
        }
    }

    private final String zza(String str) {
        String str2 = this.zzb;
        return str2 == null ? str : str2.concat(str);
    }

    public boolean canLog(int i) {
        return Log.isLoggable(this.zza, i);
    }

    public void d(String str, String str2) {
        if (canLog(3)) {
            Log.d(str, zza(str2));
        }
    }

    public void e(String str, String str2) {
        if (canLog(6)) {
            Log.e(str, zza(str2));
        }
    }

    public void e(String str, String str2, Throwable th) {
        if (canLog(6)) {
            Log.e(str, zza(str2), th);
        }
    }

    public void v(String str, String str2) {
        if (canLog(2)) {
            Log.v(str, zza(str2));
        }
    }

    public void w(String str, String str2) {
        if (canLog(5)) {
            Log.w(str, zza(str2));
        }
    }
}
