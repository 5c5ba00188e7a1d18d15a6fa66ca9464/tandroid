package org.telegram.ui;

import org.telegram.ui.Components.ItemOptions;

/* loaded from: classes4.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda258 implements Runnable {
    public final /* synthetic */ ItemOptions f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda258(ItemOptions itemOptions) {
        this.f$0 = itemOptions;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.closeSwipeback();
    }
}
