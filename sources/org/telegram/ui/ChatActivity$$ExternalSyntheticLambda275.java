package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda275 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda275 INSTANCE = new ChatActivity$$ExternalSyntheticLambda275();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda275() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$270;
        lambda$showChatThemeBottomSheet$270 = ChatActivity.lambda$showChatThemeBottomSheet$270(motionEvent);
        return lambda$showChatThemeBottomSheet$270;
    }
}
