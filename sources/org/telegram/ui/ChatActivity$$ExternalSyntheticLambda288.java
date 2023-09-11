package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda288 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda288 INSTANCE = new ChatActivity$$ExternalSyntheticLambda288();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda288() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$283;
        lambda$showChatThemeBottomSheet$283 = ChatActivity.lambda$showChatThemeBottomSheet$283(motionEvent);
        return lambda$showChatThemeBottomSheet$283;
    }
}
