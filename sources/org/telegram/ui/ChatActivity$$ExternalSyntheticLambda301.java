package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda301 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda301 INSTANCE = new ChatActivity$$ExternalSyntheticLambda301();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda301() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$296;
        lambda$showChatThemeBottomSheet$296 = ChatActivity.lambda$showChatThemeBottomSheet$296(motionEvent);
        return lambda$showChatThemeBottomSheet$296;
    }
}
