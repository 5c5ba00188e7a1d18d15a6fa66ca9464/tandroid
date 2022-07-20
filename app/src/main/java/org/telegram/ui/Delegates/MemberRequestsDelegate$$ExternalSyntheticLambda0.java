package org.telegram.ui.Delegates;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class MemberRequestsDelegate$$ExternalSyntheticLambda0 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ MemberRequestsDelegate f$0;

    public /* synthetic */ MemberRequestsDelegate$$ExternalSyntheticLambda0(MemberRequestsDelegate memberRequestsDelegate) {
        this.f$0 = memberRequestsDelegate;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$onItemClick$0(dialogInterface);
    }
}
