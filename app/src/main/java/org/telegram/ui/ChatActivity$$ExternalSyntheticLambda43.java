package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda43 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda43(ChatActivity chatActivity, boolean z) {
        this.f$0 = chatActivity;
        this.f$1 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$checkRecordLocked$219(this.f$1, dialogInterface, i);
    }
}
