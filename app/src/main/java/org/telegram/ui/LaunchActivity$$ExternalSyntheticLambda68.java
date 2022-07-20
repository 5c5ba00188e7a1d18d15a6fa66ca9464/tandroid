package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda68 implements RequestDelegate {
    public final /* synthetic */ AlertDialog f$0;
    public final /* synthetic */ ActionIntroActivity f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda68(AlertDialog alertDialog, ActionIntroActivity actionIntroActivity) {
        this.f$0 = alertDialog;
        this.f$1 = actionIntroActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        LaunchActivity.lambda$handleIntent$20(this.f$0, this.f$1, tLObject, tLRPC$TL_error);
    }
}
