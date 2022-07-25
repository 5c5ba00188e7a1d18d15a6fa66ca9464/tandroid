package com.google.android.gms.common.logging;

import android.util.Log;
import androidx.annotation.RecentlyNonNull;
import com.google.android.gms.common.internal.GmsLogger;
import java.util.Locale;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public class Logger {
    private final String zza;
    private final String zzb;
    private final int zzd;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Logger(@RecentlyNonNull String str, @RecentlyNonNull String... strArr) {
        this(str, r8);
        String sb;
        if (strArr.length == 0) {
            sb = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append('[');
            for (String str2 : strArr) {
                if (sb2.length() > 1) {
                    sb2.append(",");
                }
                sb2.append(str2);
            }
            sb2.append(']');
            sb2.append(' ');
            sb = sb2.toString();
        }
    }

    private Logger(String str, String str2) {
        this.zzb = str2;
        this.zza = str;
        new GmsLogger(str);
        int i = 2;
        while (7 >= i && !Log.isLoggable(this.zza, i)) {
            i++;
        }
        this.zzd = i;
    }

    public boolean isLoggable(int i) {
        return this.zzd <= i;
    }

    public void d(@RecentlyNonNull String str, Object... objArr) {
        if (isLoggable(3)) {
            Log.d(this.zza, format(str, objArr));
        }
    }

    public void e(@RecentlyNonNull String str, Object... objArr) {
        Log.e(this.zza, format(str, objArr));
    }

    @RecentlyNonNull
    protected String format(@RecentlyNonNull String str, Object... objArr) {
        if (objArr != null && objArr.length > 0) {
            str = String.format(Locale.US, str, objArr);
        }
        return this.zzb.concat(str);
    }
}
