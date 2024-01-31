package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda326 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda326 INSTANCE = new ChatActivity$$ExternalSyntheticLambda326();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda326() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$315;
        lambda$showChatThemeBottomSheet$315 = ChatActivity.lambda$showChatThemeBottomSheet$315(motionEvent);
        return lambda$showChatThemeBottomSheet$315;
    }
}
