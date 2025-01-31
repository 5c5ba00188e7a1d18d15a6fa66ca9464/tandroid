package com.google.android.play.core.integrity;

import android.content.Context;

/* loaded from: classes.dex */
final class ax {
    private static aw a;

    static synchronized aw a(Context context) {
        aw awVar;
        synchronized (ax.class) {
            try {
                if (a == null) {
                    u uVar = new u(null);
                    uVar.a(com.google.android.play.integrity.internal.ag.a(context));
                    a = uVar.b();
                }
                awVar = a;
            } catch (Throwable th) {
                throw th;
            }
        }
        return awVar;
    }
}
