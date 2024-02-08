package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda325 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda325 INSTANCE = new ChatActivity$$ExternalSyntheticLambda325();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda325() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$314;
        lambda$showChatThemeBottomSheet$314 = ChatActivity.lambda$showChatThemeBottomSheet$314(motionEvent);
        return lambda$showChatThemeBottomSheet$314;
    }
}
