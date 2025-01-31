package com.google.android.play.core.integrity;

import android.content.Context;

/* loaded from: classes.dex */
final class z {
    private static s a;

    static synchronized s a(Context context) {
        s sVar;
        synchronized (z.class) {
            try {
                if (a == null) {
                    q qVar = new q(null);
                    qVar.a(com.google.android.play.integrity.internal.ag.a(context));
                    a = qVar.b();
                }
                sVar = a;
            } catch (Throwable th) {
                throw th;
            }
        }
        return sVar;
    }
}
