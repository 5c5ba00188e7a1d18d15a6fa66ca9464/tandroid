package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda27 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda27(BaseFragment baseFragment, String str, long j, boolean z) {
        this.f$0 = baseFragment;
        this.f$1 = str;
        this.f$2 = j;
        this.f$3 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$showOpenUrlAlert$17(this.f$0, this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}
