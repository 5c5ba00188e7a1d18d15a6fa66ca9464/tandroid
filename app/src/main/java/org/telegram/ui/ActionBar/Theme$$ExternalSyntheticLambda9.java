package org.telegram.ui.ActionBar;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class Theme$$ExternalSyntheticLambda9 implements RequestDelegate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ Theme$$ExternalSyntheticLambda9(int i) {
        this.f$0 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        Theme.lambda$loadRemoteThemes$6(this.f$0, tLObject, tLRPC$TL_error);
    }
}
