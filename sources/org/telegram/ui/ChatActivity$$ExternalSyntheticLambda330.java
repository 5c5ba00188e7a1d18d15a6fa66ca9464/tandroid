package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda330 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda330 INSTANCE = new ChatActivity$$ExternalSyntheticLambda330();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda330() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$319;
        lambda$showChatThemeBottomSheet$319 = ChatActivity.lambda$showChatThemeBottomSheet$319(motionEvent);
        return lambda$showChatThemeBottomSheet$319;
    }
}
