package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import android.util.Log;

/* loaded from: classes.dex */
public abstract class zzb extends zza {
    public static Boolean zza(SharedPreferences sharedPreferences, String str, Boolean bool) {
        try {
            return (Boolean) com.google.android.gms.internal.flags.zze.zza(new zzc(sharedPreferences, str, bool));
        } catch (Exception e) {
            String valueOf = String.valueOf(e.getMessage());
            Log.w("FlagDataUtils", valueOf.length() != 0 ? "Flag value not available, returning default: ".concat(valueOf) : new String("Flag value not available, returning default: "));
            return bool;
        }
    }
}
