package com.google.android.gms.internal.base;

import android.os.Build;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public abstract class zap {
    public static final int zaa;

    static {
        zaa = Build.VERSION.SDK_INT >= 31 ? ConnectionsManager.FileTypeVideo : 0;
    }
}
