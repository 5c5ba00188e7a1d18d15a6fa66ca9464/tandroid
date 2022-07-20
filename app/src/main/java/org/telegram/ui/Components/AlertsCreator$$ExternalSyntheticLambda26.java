package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda26 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BaseFragment f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda26(BaseFragment baseFragment) {
        this.f$0 = baseFragment;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.performAskAQuestion(this.f$0);
    }
}
