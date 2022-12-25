package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda260 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda260 INSTANCE = new ChatActivity$$ExternalSyntheticLambda260();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda260() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$261;
        lambda$showChatThemeBottomSheet$261 = ChatActivity.lambda$showChatThemeBottomSheet$261(motionEvent);
        return lambda$showChatThemeBottomSheet$261;
    }
}
