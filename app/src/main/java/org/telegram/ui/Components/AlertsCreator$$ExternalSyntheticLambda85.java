package org.telegram.ui.Components;

import android.content.SharedPreferences;
import org.telegram.tgnet.TLRPC$TL_help_support;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda85 implements Runnable {
    public final /* synthetic */ SharedPreferences f$0;
    public final /* synthetic */ TLRPC$TL_help_support f$1;
    public final /* synthetic */ AlertDialog f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ BaseFragment f$4;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda85(SharedPreferences sharedPreferences, TLRPC$TL_help_support tLRPC$TL_help_support, AlertDialog alertDialog, int i, BaseFragment baseFragment) {
        this.f$0 = sharedPreferences;
        this.f$1 = tLRPC$TL_help_support;
        this.f$2 = alertDialog;
        this.f$3 = i;
        this.f$4 = baseFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AlertsCreator.lambda$performAskAQuestion$19(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
