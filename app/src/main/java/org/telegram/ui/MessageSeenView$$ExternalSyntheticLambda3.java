package org.telegram.ui;

import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessageSeenView$$ExternalSyntheticLambda3 implements RequestDelegate {
    public final /* synthetic */ MessageSeenView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ HashMap f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ MessageSeenView$$ExternalSyntheticLambda3(MessageSeenView messageSeenView, int i, HashMap hashMap, ArrayList arrayList) {
        this.f$0 = messageSeenView;
        this.f$1 = i;
        this.f$2 = hashMap;
        this.f$3 = arrayList;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$new$3(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}
