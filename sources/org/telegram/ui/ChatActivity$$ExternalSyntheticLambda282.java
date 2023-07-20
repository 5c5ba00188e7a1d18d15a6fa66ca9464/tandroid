package org.telegram.ui;

import android.view.MotionEvent;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda282 implements RecyclerListView.OnInterceptTouchListener {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda282 INSTANCE = new ChatActivity$$ExternalSyntheticLambda282();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda282() {
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnInterceptTouchListener
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean lambda$showChatThemeBottomSheet$277;
        lambda$showChatThemeBottomSheet$277 = ChatActivity.lambda$showChatThemeBottomSheet$277(motionEvent);
        return lambda$showChatThemeBottomSheet$277;
    }
}
