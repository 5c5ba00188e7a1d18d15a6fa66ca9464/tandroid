package org.telegram.messenger;

import org.telegram.messenger.LocaleController;
/* loaded from: classes.dex */
public final /* synthetic */ class LocaleController$TimeZoneChangedReceiver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LocaleController.TimeZoneChangedReceiver f$0;

    public /* synthetic */ LocaleController$TimeZoneChangedReceiver$$ExternalSyntheticLambda0(LocaleController.TimeZoneChangedReceiver timeZoneChangedReceiver) {
        this.f$0 = timeZoneChangedReceiver;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onReceive$0();
    }
}
