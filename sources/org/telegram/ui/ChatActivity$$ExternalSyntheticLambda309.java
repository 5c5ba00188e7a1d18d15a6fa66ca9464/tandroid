package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda309 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda309 INSTANCE = new ChatActivity$$ExternalSyntheticLambda309();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda309() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$299;
        lambda$showChatThemeBottomSheet$299 = ChatActivity.lambda$showChatThemeBottomSheet$299(motionEvent);
        return lambda$showChatThemeBottomSheet$299;
    }
}
