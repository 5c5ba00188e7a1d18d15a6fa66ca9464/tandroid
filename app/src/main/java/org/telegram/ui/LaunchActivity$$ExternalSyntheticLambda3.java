package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda3 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ boolean[] f$0;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda3(boolean[] zArr) {
        this.f$0 = zArr;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        LaunchActivity.lambda$runLinkRequest$45(this.f$0, dialogInterface);
    }
}
