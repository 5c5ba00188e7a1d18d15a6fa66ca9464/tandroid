package org.telegram.ui;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda5 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda5(PassportActivity passportActivity, ArrayList arrayList) {
        this.f$0 = passportActivity;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$openAddDocumentAlert$23(this.f$1, dialogInterface, i);
    }
}
