package com.google.android.play.core.integrity;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class ar extends ResultReceiver {
    final /* synthetic */ as a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    ar(as asVar, Handler handler) {
        super(handler);
        this.a = asVar;
    }

    @Override // android.os.ResultReceiver
    public final void onReceiveResult(int i, Bundle bundle) {
        TaskCompletionSource taskCompletionSource;
        int i2;
        if (i == 1) {
            taskCompletionSource = this.a.a;
            i2 = 3;
        } else if (i == 2) {
            taskCompletionSource = this.a.a;
            i2 = 2;
        } else {
            if (i != 3) {
                return;
            }
            taskCompletionSource = this.a.a;
            i2 = 1;
        }
        taskCompletionSource.trySetResult(i2);
    }
}
