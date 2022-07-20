package org.telegram.ui.Components.Premium;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class LimitReachedBottomSheet$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ LimitReachedBottomSheet f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ LimitReachedBottomSheet$$ExternalSyntheticLambda5(LimitReachedBottomSheet limitReachedBottomSheet, TLObject tLObject) {
        this.f$0 = limitReachedBottomSheet;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadAdminedChannels$5(this.f$1);
    }
}
