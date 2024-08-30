package com.google.android.play.core.integrity;

import android.content.Context;
/* loaded from: classes.dex */
public abstract class IntegrityManagerFactory {
    public static IntegrityManager create(Context context) {
        return z.a(context).a();
    }
}
