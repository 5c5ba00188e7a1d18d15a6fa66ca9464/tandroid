package com.google.android.gms.internal.location;

import java.text.SimpleDateFormat;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class zzdj {
    private static final SimpleDateFormat zza;
    private static final SimpleDateFormat zzb;
    private static final StringBuilder zzc;

    static {
        Locale locale = Locale.ROOT;
        zza = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", locale);
        zzb = new SimpleDateFormat("MM-dd HH:mm:ss", locale);
        zzc = new StringBuilder(33);
    }

    public static String zza(long j) {
        String sb;
        StringBuilder sb2 = zzc;
        synchronized (sb2) {
            sb2.setLength(0);
            zzb(j, sb2);
            sb = sb2.toString();
        }
        return sb;
    }

    public static void zzb(long j, StringBuilder sb) {
        String str;
        if (j == 0) {
            str = "0s";
        } else {
            sb.ensureCapacity(sb.length() + 27);
            boolean z = false;
            if (j < 0) {
                sb.append("-");
                if (j != Long.MIN_VALUE) {
                    j = -j;
                } else {
                    j = Long.MAX_VALUE;
                    z = true;
                }
            }
            if (j >= 86400000) {
                sb.append(j / 86400000);
                sb.append("d");
                j %= 86400000;
            }
            if (true == z) {
                j = 25975808;
            }
            if (j >= 3600000) {
                sb.append(j / 3600000);
                sb.append("h");
                j %= 3600000;
            }
            if (j >= 60000) {
                sb.append(j / 60000);
                sb.append("m");
                j %= 60000;
            }
            if (j >= 1000) {
                sb.append(j / 1000);
                sb.append("s");
                j %= 1000;
            }
            if (j <= 0) {
                return;
            }
            sb.append(j);
            str = "ms";
        }
        sb.append(str);
    }
}
