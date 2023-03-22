package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda276 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda276 INSTANCE = new ChatActivity$$ExternalSyntheticLambda276();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda276() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$271;
        lambda$showChatThemeBottomSheet$271 = ChatActivity.lambda$showChatThemeBottomSheet$271(motionEvent);
        return lambda$showChatThemeBottomSheet$271;
    }
}
