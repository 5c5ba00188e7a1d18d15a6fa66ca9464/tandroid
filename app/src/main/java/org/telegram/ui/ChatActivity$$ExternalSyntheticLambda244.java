package org.telegram.ui;

import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda244 implements ReactedUsersListView.OnProfileSelectedListener {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda244(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // org.telegram.ui.Components.ReactedUsersListView.OnProfileSelectedListener
    public final void onProfileSelected(ReactedUsersListView reactedUsersListView, long j) {
        this.f$0.lambda$createMenu$158(reactedUsersListView, j);
    }
}
