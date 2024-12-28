package org.telegram.ui;

import org.telegram.messenger.browser.Browser;

/* loaded from: classes4.dex */
public final /* synthetic */ class ChatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ Browser.Progress f$0;

    public /* synthetic */ ChatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda4(Browser.Progress progress) {
        this.f$0 = progress;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.end();
    }
}
