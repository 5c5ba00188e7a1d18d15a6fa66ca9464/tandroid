package org.telegram.ui;

import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda4 implements ReactedUsersListView.OnProfileSelectedListener {
    public final /* synthetic */ ChatActivity.ChatActivityAdapter.AnonymousClass1 f$0;

    public /* synthetic */ ChatActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda4(ChatActivity.ChatActivityAdapter.AnonymousClass1 anonymousClass1) {
        this.f$0 = anonymousClass1;
    }

    @Override // org.telegram.ui.Components.ReactedUsersListView.OnProfileSelectedListener
    public final void onProfileSelected(ReactedUsersListView reactedUsersListView, long j) {
        this.f$0.lambda$didPressReaction$3(reactedUsersListView, j);
    }
}
