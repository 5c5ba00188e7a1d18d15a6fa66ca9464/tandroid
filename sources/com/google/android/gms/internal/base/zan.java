package com.google.android.gms.internal.base;

import android.os.Build;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zan {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zaa() {
        return Build.VERSION.SDK_INT >= 33 || Build.VERSION.CODENAME.charAt(0) == 'T';
    }
}
