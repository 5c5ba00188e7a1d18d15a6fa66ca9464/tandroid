package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$2$$ExternalSyntheticLambda2 implements RequestDelegate {
    public final /* synthetic */ WallpapersListActivity.AnonymousClass2 f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ WallpapersListActivity$2$$ExternalSyntheticLambda2(WallpapersListActivity.AnonymousClass2 anonymousClass2, int[] iArr) {
        this.f$0 = anonymousClass2;
        this.f$1 = iArr;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onItemClick$1(this.f$1, tLObject, tLRPC$TL_error);
    }
}
