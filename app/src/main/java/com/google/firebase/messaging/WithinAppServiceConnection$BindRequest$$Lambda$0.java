package com.google.firebase.messaging;

import com.google.firebase.messaging.WithinAppServiceConnection;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class WithinAppServiceConnection$BindRequest$$Lambda$0 implements Runnable {
    private final WithinAppServiceConnection.BindRequest arg$1;

    public WithinAppServiceConnection$BindRequest$$Lambda$0(WithinAppServiceConnection.BindRequest bindRequest) {
        this.arg$1 = bindRequest;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.arg$1.lambda$arrangeTimeout$0$WithinAppServiceConnection$BindRequest();
    }
}
