package org.telegram.ui;

import org.telegram.messenger.CallReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35 implements Runnable {
    public static final /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35 INSTANCE = new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35();

    private /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        CallReceiver.checkLastReceivedCall();
    }
}
