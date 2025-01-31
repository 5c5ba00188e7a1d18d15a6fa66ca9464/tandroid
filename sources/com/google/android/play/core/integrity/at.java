package com.google.android.play.core.integrity;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class at {
    private final com.google.android.play.integrity.internal.an a;
    private final com.google.android.play.integrity.internal.an b;

    at(com.google.android.play.integrity.internal.an anVar, com.google.android.play.integrity.internal.an anVar2) {
        this.a = anVar;
        this.b = anVar2;
    }

    final as a(Activity activity, TaskCompletionSource taskCompletionSource, com.google.android.play.integrity.internal.ae aeVar) {
        Object a = this.a.a();
        a.getClass();
        k kVar = (k) this.b.a();
        kVar.getClass();
        activity.getClass();
        aeVar.getClass();
        return new as((Context) a, kVar, activity, taskCompletionSource, aeVar);
    }
}
