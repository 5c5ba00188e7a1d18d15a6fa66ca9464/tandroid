package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionBottomSheet$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ SessionBottomSheet f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ SessionBottomSheet$$ExternalSyntheticLambda0(SessionBottomSheet sessionBottomSheet, String str) {
        this.f$0 = sessionBottomSheet;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$copyText$1(this.f$1, dialogInterface, i);
    }
}
