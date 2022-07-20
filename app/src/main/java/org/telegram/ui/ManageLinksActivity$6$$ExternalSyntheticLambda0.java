package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.ManageLinksActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ManageLinksActivity$6$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ManageLinksActivity.AnonymousClass6 f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ ManageLinksActivity$6$$ExternalSyntheticLambda0(ManageLinksActivity.AnonymousClass6 anonymousClass6, TLObject tLObject) {
        this.f$0 = anonymousClass6;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onLinkCreated$0(this.f$1);
    }
}
