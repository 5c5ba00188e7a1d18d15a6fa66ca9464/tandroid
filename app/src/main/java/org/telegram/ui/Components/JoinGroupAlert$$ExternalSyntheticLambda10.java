package org.telegram.ui.Components;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class JoinGroupAlert$$ExternalSyntheticLambda10 implements MessagesController.ErrorDelegate {
    public final /* synthetic */ JoinGroupAlert f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ JoinGroupAlert$$ExternalSyntheticLambda10(JoinGroupAlert joinGroupAlert, boolean z) {
        this.f$0 = joinGroupAlert;
        this.f$1 = z;
    }

    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
        boolean lambda$new$3;
        lambda$new$3 = this.f$0.lambda$new$3(this.f$1, tLRPC$TL_error);
        return lambda$new$3;
    }
}
