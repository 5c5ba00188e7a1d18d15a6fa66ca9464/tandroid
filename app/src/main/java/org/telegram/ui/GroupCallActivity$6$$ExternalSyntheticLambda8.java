package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$$ExternalSyntheticLambda8 implements RequestDelegate {
    public final /* synthetic */ GroupCallActivity.AnonymousClass6 f$0;

    public /* synthetic */ GroupCallActivity$6$$ExternalSyntheticLambda8(GroupCallActivity.AnonymousClass6 anonymousClass6) {
        this.f$0 = anonymousClass6;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onItemClick$0(tLObject, tLRPC$TL_error);
    }
}
