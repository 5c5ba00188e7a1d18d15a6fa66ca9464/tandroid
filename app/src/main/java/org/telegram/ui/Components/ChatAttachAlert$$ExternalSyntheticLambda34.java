package org.telegram.ui.Components;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.ui.Components.ChatAttachAlertPollLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda34 implements ChatAttachAlertPollLayout.PollCreateActivityDelegate {
    public final /* synthetic */ ChatAttachAlert f$0;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda34(ChatAttachAlert chatAttachAlert) {
        this.f$0 = chatAttachAlert;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertPollLayout.PollCreateActivityDelegate
    public final void sendPoll(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
        this.f$0.lambda$new$6(tLRPC$TL_messageMediaPoll, hashMap, z, i);
    }
}
