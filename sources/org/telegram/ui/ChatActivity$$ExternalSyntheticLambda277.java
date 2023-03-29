package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda277 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda277 INSTANCE = new ChatActivity$$ExternalSyntheticLambda277();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda277() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$272;
        lambda$showChatThemeBottomSheet$272 = ChatActivity.lambda$showChatThemeBottomSheet$272(motionEvent);
        return lambda$showChatThemeBottomSheet$272;
    }
}
