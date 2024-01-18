package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda311 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda311 INSTANCE = new ChatActivity$$ExternalSyntheticLambda311();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda311() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$301;
        lambda$showChatThemeBottomSheet$301 = ChatActivity.lambda$showChatThemeBottomSheet$301(motionEvent);
        return lambda$showChatThemeBottomSheet$301;
    }
}
