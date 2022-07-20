package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda11 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda11(PassportActivity passportActivity, boolean[] zArr) {
        this.f$0 = passportActivity;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createDocumentDeleteAlert$38(this.f$1, dialogInterface, i);
    }
}
