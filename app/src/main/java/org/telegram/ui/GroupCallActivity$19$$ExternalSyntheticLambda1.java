package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$19$$ExternalSyntheticLambda1 implements RequestDelegate {
    public final /* synthetic */ GroupCallActivity.AnonymousClass19 f$0;

    public /* synthetic */ GroupCallActivity$19$$ExternalSyntheticLambda1(GroupCallActivity.AnonymousClass19 anonymousClass19) {
        this.f$0 = anonymousClass19;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onClick$1(tLObject, tLRPC$TL_error);
    }
}
