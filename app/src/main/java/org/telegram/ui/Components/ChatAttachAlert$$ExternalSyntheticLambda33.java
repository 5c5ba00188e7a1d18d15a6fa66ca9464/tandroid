package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.ui.Components.ChatAttachAlertLocationLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda33 implements ChatAttachAlertLocationLayout.LocationActivityDelegate {
    public final /* synthetic */ ChatAttachAlert f$0;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda33(ChatAttachAlert chatAttachAlert) {
        this.f$0 = chatAttachAlert;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertLocationLayout.LocationActivityDelegate
    public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        this.f$0.lambda$new$5(tLRPC$MessageMedia, i, z, i2);
    }
}
