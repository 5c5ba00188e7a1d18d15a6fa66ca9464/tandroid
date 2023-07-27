package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda283 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda283 INSTANCE = new ChatActivity$$ExternalSyntheticLambda283();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda283() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$278;
        lambda$showChatThemeBottomSheet$278 = ChatActivity.lambda$showChatThemeBottomSheet$278(motionEvent);
        return lambda$showChatThemeBottomSheet$278;
    }
}
