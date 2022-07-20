package org.telegram.ui;

import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$102$$ExternalSyntheticLambda1 implements ReactedUsersListView.OnProfileSelectedListener {
    public final /* synthetic */ ChatActivity.AnonymousClass102 f$0;

    public /* synthetic */ ChatActivity$102$$ExternalSyntheticLambda1(ChatActivity.AnonymousClass102 anonymousClass102) {
        this.f$0 = anonymousClass102;
    }

    @Override // org.telegram.ui.Components.ReactedUsersListView.OnProfileSelectedListener
    public final void onProfileSelected(ReactedUsersListView reactedUsersListView, long j) {
        this.f$0.lambda$instantiateItem$0(reactedUsersListView, j);
    }
}
