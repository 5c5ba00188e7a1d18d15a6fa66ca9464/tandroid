package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatNotificationsPopupWrapper$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ ChatNotificationsPopupWrapper f$0;
    public final /* synthetic */ ChatNotificationsPopupWrapper.Callback f$1;

    public /* synthetic */ ChatNotificationsPopupWrapper$$ExternalSyntheticLambda5(ChatNotificationsPopupWrapper chatNotificationsPopupWrapper, ChatNotificationsPopupWrapper.Callback callback) {
        this.f$0 = chatNotificationsPopupWrapper;
        this.f$1 = callback;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$new$2(this.f$1, view);
    }
}
