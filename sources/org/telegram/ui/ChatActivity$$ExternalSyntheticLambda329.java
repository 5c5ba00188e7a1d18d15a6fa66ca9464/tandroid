package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda329 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda329 INSTANCE = new ChatActivity$$ExternalSyntheticLambda329();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda329() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$318;
        lambda$showChatThemeBottomSheet$318 = ChatActivity.lambda$showChatThemeBottomSheet$318(motionEvent);
        return lambda$showChatThemeBottomSheet$318;
    }
}
