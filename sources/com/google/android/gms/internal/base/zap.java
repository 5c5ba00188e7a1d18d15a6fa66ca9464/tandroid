package com.google.android.gms.internal.base;

import android.os.Build;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public final class zap {
    public static final int zaa;

    static {
        zaa = Build.VERSION.SDK_INT >= 31 ? ConnectionsManager.FileTypeVideo : 0;
    }
}
