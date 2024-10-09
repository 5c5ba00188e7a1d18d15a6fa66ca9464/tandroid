package com.google.android.gms.flags.impl;

import android.content.Context;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class zzk implements Callable {
    private final /* synthetic */ Context val$context;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzk(Context context) {
        this.val$context = context;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Object call() {
        return this.val$context.getSharedPreferences("google_sdk_flags", 0);
    }
}
