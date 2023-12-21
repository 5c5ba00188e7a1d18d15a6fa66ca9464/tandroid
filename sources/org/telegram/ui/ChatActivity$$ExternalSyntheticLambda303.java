package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda303 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda303 INSTANCE = new ChatActivity$$ExternalSyntheticLambda303();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda303() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$297;
        lambda$showChatThemeBottomSheet$297 = ChatActivity.lambda$showChatThemeBottomSheet$297(motionEvent);
        return lambda$showChatThemeBottomSheet$297;
    }
}
