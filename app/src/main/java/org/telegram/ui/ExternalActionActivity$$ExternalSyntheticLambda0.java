package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ExternalActionActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ ExternalActionActivity$$ExternalSyntheticLambda0(int i, int[] iArr) {
        this.f$0 = i;
        this.f$1 = iArr;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        ExternalActionActivity.lambda$handleIntent$5(this.f$0, this.f$1, dialogInterface);
    }
}
