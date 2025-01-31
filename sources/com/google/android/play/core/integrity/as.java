package com.google.android.play.core.integrity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.play.core.common.PlayCoreDialogWrapperActivity;

/* loaded from: classes.dex */
final class as extends com.google.android.play.integrity.internal.q {
    final TaskCompletionSource a;
    final com.google.android.play.integrity.internal.ae b;
    private final com.google.android.play.integrity.internal.s c = new com.google.android.play.integrity.internal.s("RequestDialogCallbackImpl");
    private final String d;
    private final k e;
    private final Activity f;

    as(Context context, k kVar, Activity activity, TaskCompletionSource taskCompletionSource, com.google.android.play.integrity.internal.ae aeVar) {
        this.d = context.getPackageName();
        this.e = kVar;
        this.a = taskCompletionSource;
        this.f = activity;
        this.b = aeVar;
    }

    @Override // com.google.android.play.integrity.internal.r
    public final void b(Bundle bundle) {
        this.b.v(this.a);
        this.c.d("onRequestDialog(%s)", this.d);
        ApiException a = this.e.a(bundle);
        if (a != null) {
            this.a.trySetException(a);
            return;
        }
        PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("dialog.intent");
        if (pendingIntent == null) {
            this.c.b("onRequestDialog(%s): got null dialog intent", this.d);
            this.a.trySetResult(0);
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                this.c.e("Integrity Dialog requires minimum SDK version: %d", 21);
                this.a.trySetResult(0);
                return;
            }
            Intent intent = new Intent(this.f, (Class<?>) PlayCoreDialogWrapperActivity.class);
            intent.putExtra("confirmation_intent", pendingIntent);
            intent.setFlags(536870912);
            intent.putExtra("result_receiver", new ar(this, this.b.c()));
            this.c.a("Starting dialog intent...", new Object[0]);
            this.f.startActivityForResult(intent, 0);
        }
    }
}
