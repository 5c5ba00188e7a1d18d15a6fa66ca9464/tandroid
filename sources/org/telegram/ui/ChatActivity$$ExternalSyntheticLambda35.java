package org.telegram.ui;

import org.telegram.ui.Components.ScrimOptions;

/* loaded from: classes4.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda35 implements Runnable {
    public final /* synthetic */ ScrimOptions f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda35(ScrimOptions scrimOptions) {
        this.f$0 = scrimOptions;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.dismissFast();
    }
}
