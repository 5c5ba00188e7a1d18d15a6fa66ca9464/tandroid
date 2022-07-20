package org.telegram.ui;

import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$26$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DialogsActivity.AnonymousClass26 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ TLRPC$Dialog f$2;

    public /* synthetic */ DialogsActivity$26$$ExternalSyntheticLambda0(DialogsActivity.AnonymousClass26 anonymousClass26, int i, TLRPC$Dialog tLRPC$Dialog) {
        this.f$0 = anonymousClass26;
        this.f$1 = i;
        this.f$2 = tLRPC$Dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRemoveDialogAction$0(this.f$1, this.f$2);
    }
}
