package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda11 implements RequestDelegate {
    public final /* synthetic */ FileRefController f$0;

    public /* synthetic */ FileRefController$$ExternalSyntheticLambda11(FileRefController fileRefController) {
        this.f$0 = fileRefController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$requestReferenceFromServer$13(tLObject, tLRPC$TL_error);
    }
}
