package com.google.android.play.integrity.internal;

import android.content.Context;

/* loaded from: classes.dex */
public abstract class ag {
    public static Context a(Context context) {
        Context applicationContext = context.getApplicationContext();
        return applicationContext != null ? applicationContext : context;
    }
}
