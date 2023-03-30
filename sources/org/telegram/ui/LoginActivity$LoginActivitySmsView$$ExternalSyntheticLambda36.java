package org.telegram.ui;

import org.telegram.messenger.CallReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36 implements Runnable {
    public static final /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36 INSTANCE = new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36();

    private /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        CallReceiver.checkLastReceivedCall();
    }
}
