package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda271 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda271 INSTANCE = new ChatActivity$$ExternalSyntheticLambda271();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda271() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$266;
        lambda$showChatThemeBottomSheet$266 = ChatActivity.lambda$showChatThemeBottomSheet$266(motionEvent);
        return lambda$showChatThemeBottomSheet$266;
    }
}
