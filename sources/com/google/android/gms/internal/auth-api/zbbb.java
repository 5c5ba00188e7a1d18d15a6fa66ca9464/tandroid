package com.google.android.gms.internal.auth-api;

import android.util.Base64;
import java.util.Random;

/* loaded from: classes.dex */
public abstract class zbbb {
    private static final Random zba = new Random();

    public static String zba() {
        byte[] bArr = new byte[16];
        zba.nextBytes(bArr);
        return Base64.encodeToString(bArr, 11);
    }
}
