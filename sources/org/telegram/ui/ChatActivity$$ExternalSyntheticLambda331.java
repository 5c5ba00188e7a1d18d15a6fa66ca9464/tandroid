package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda331 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda331 INSTANCE = new ChatActivity$$ExternalSyntheticLambda331();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda331() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$320;
        lambda$showChatThemeBottomSheet$320 = ChatActivity.lambda$showChatThemeBottomSheet$320(motionEvent);
        return lambda$showChatThemeBottomSheet$320;
    }
}
