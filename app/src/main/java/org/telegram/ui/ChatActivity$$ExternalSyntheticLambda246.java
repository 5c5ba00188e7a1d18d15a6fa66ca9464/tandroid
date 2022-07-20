package org.telegram.ui;

import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda246 implements RecyclerAnimationScrollHelper.ScrollListener {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda246(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
    public final void onScroll() {
        this.f$0.invalidateMessagesVisiblePart();
    }
}
