package com.google.android.play.integrity.internal;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: classes.dex */
public abstract class m extends b implements n {
    public static n b(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.play.core.integrity.protocol.IIntegrityService");
        return queryLocalInterface instanceof n ? (n) queryLocalInterface : new l(iBinder);
    }
}
