package org.telegram.ui.Components;

import android.content.SharedPreferences;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda93 implements RequestDelegate {
    public final /* synthetic */ SharedPreferences f$0;
    public final /* synthetic */ AlertDialog f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda93(SharedPreferences sharedPreferences, AlertDialog alertDialog, int i, BaseFragment baseFragment) {
        this.f$0 = sharedPreferences;
        this.f$1 = alertDialog;
        this.f$2 = i;
        this.f$3 = baseFragment;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AlertsCreator.lambda$performAskAQuestion$21(this.f$0, this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}
