package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda245 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda245 INSTANCE = new ChatActivity$$ExternalSyntheticLambda245();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda245() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$246;
        lambda$showChatThemeBottomSheet$246 = ChatActivity.lambda$showChatThemeBottomSheet$246(motionEvent);
        return lambda$showChatThemeBottomSheet$246;
    }
}
