package com.google.android.gms.internal.base;

import android.os.Build;

/* loaded from: classes.dex */
abstract class zan {
    static boolean zaa() {
        return Build.VERSION.SDK_INT >= 33 || Build.VERSION.CODENAME.charAt(0) == 'T';
    }
}
