package com.google.android.gms.internal.common;

import android.os.Build;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
/* loaded from: classes.dex */
public final class zzd {
    public static final int zza;

    static {
        zza = Build.VERSION.SDK_INT >= 23 ? ConnectionsManager.FileTypeFile : 0;
    }
}
