package com.google.android.play.core.integrity;

import android.content.Context;
import com.google.android.play.integrity.internal.ag;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class z {
    private static s a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized s a(Context context) {
        s sVar;
        synchronized (z.class) {
            try {
                if (a == null) {
                    q qVar = new q(null);
                    qVar.a(ag.a(context));
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
