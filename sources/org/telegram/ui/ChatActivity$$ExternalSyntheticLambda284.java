package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda284 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda284 INSTANCE = new ChatActivity$$ExternalSyntheticLambda284();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda284() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$279;
        lambda$showChatThemeBottomSheet$279 = ChatActivity.lambda$showChatThemeBottomSheet$279(motionEvent);
        return lambda$showChatThemeBottomSheet$279;
    }
}
