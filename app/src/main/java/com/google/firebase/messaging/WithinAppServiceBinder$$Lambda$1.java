package com.google.firebase.messaging;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.WithinAppServiceConnection;
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class WithinAppServiceBinder$$Lambda$1 implements OnCompleteListener {
    private final WithinAppServiceConnection.BindRequest arg$1;

    public WithinAppServiceBinder$$Lambda$1(WithinAppServiceConnection.BindRequest bindRequest) {
        this.arg$1 = bindRequest;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public void onComplete(Task task) {
        this.arg$1.finish();
    }
}
