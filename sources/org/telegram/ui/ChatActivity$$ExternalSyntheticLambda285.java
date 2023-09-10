package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda285 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda285 INSTANCE = new ChatActivity$$ExternalSyntheticLambda285();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda285() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$280;
        lambda$showChatThemeBottomSheet$280 = ChatActivity.lambda$showChatThemeBottomSheet$280(motionEvent);
        return lambda$showChatThemeBottomSheet$280;
    }
}
