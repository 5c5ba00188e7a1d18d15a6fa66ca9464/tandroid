package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda279 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda279 INSTANCE = new ChatActivity$$ExternalSyntheticLambda279();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda279() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$273;
        lambda$showChatThemeBottomSheet$273 = ChatActivity.lambda$showChatThemeBottomSheet$273(motionEvent);
        return lambda$showChatThemeBottomSheet$273;
    }
}
