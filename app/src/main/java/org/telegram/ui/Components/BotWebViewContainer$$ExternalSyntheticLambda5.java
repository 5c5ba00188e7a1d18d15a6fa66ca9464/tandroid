package org.telegram.ui.Components;

import android.content.DialogInterface;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewContainer$$ExternalSyntheticLambda5 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ BotWebViewContainer f$0;
    public final /* synthetic */ AtomicBoolean f$1;

    public /* synthetic */ BotWebViewContainer$$ExternalSyntheticLambda5(BotWebViewContainer botWebViewContainer, AtomicBoolean atomicBoolean) {
        this.f$0 = botWebViewContainer;
        this.f$1 = atomicBoolean;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$onEventReceived$12(this.f$1, dialogInterface);
    }
}
