package org.telegram.ui.Components.voip;

import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPHelper$$ExternalSyntheticLambda17 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ TLRPC$Chat f$0;
    public final /* synthetic */ TLRPC$InputPeer f$1;
    public final /* synthetic */ BaseFragment f$2;
    public final /* synthetic */ AccountInstance f$3;

    public /* synthetic */ VoIPHelper$$ExternalSyntheticLambda17(TLRPC$Chat tLRPC$Chat, TLRPC$InputPeer tLRPC$InputPeer, BaseFragment baseFragment, AccountInstance accountInstance) {
        this.f$0 = tLRPC$Chat;
        this.f$1 = tLRPC$InputPeer;
        this.f$2 = baseFragment;
        this.f$3 = accountInstance;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        VoIPHelper.lambda$showGroupCallAlert$20(this.f$0, this.f$1, this.f$2, this.f$3, z);
    }
}
