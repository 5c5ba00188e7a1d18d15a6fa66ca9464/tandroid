package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterCreateActivity$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ FilterCreateActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ FilterCreateActivity$$ExternalSyntheticLambda4(FilterCreateActivity filterCreateActivity, int i, boolean z) {
        this.f$0 = filterCreateActivity;
        this.f$1 = i;
        this.f$2 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showRemoveAlert$9(this.f$1, this.f$2, dialogInterface, i);
    }
}
