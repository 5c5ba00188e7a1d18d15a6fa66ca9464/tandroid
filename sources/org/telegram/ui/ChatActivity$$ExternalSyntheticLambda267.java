package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda267 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda267 INSTANCE = new ChatActivity$$ExternalSyntheticLambda267();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda267() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$262;
        lambda$showChatThemeBottomSheet$262 = ChatActivity.lambda$showChatThemeBottomSheet$262(motionEvent);
        return lambda$showChatThemeBottomSheet$262;
    }
}
