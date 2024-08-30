package com.google.android.gms.common.util;

import java.util.regex.Pattern;
/* loaded from: classes.dex */
public abstract class Strings {
    private static final Pattern zza = Pattern.compile("\\$\\{(.*?)\\}");

    public static boolean isEmptyOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }
}
