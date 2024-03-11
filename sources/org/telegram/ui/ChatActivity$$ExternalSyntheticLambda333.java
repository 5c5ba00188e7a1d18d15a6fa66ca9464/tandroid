package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda333 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda333 INSTANCE = new ChatActivity$$ExternalSyntheticLambda333();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda333() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$322;
        lambda$showChatThemeBottomSheet$322 = ChatActivity.lambda$showChatThemeBottomSheet$322(motionEvent);
        return lambda$showChatThemeBottomSheet$322;
    }
}
