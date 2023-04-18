package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda280 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda280 INSTANCE = new ChatActivity$$ExternalSyntheticLambda280();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda280() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$275;
        lambda$showChatThemeBottomSheet$275 = ChatActivity.lambda$showChatThemeBottomSheet$275(motionEvent);
        return lambda$showChatThemeBottomSheet$275;
    }
}
