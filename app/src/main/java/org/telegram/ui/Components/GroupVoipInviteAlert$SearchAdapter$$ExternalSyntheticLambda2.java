package org.telegram.ui.Components;

import org.telegram.ui.Components.GroupVoipInviteAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupVoipInviteAlert$SearchAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ GroupVoipInviteAlert.SearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ GroupVoipInviteAlert$SearchAdapter$$ExternalSyntheticLambda2(GroupVoipInviteAlert.SearchAdapter searchAdapter, String str, int i) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$2(this.f$1, this.f$2);
    }
}
