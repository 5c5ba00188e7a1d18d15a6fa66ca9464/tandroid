package org.telegram.ui;

import org.telegram.ui.PrivacyControlActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyControlActivity$MessageCell$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PrivacyControlActivity.MessageCell f$0;

    public /* synthetic */ PrivacyControlActivity$MessageCell$$ExternalSyntheticLambda0(PrivacyControlActivity.MessageCell messageCell) {
        this.f$0 = messageCell;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.invalidate();
    }
}
