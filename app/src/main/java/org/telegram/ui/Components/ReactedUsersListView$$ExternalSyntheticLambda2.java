package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactedUsersListView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ReactedUsersListView f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ ReactedUsersListView$$ExternalSyntheticLambda2(ReactedUsersListView reactedUsersListView, TLObject tLObject) {
        this.f$0 = reactedUsersListView;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$load$4(this.f$1);
    }
}
