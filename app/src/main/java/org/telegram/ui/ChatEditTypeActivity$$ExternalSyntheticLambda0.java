package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$Chat;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatEditTypeActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChatEditTypeActivity f$0;
    public final /* synthetic */ TLRPC$Chat f$1;

    public /* synthetic */ ChatEditTypeActivity$$ExternalSyntheticLambda0(ChatEditTypeActivity chatEditTypeActivity, TLRPC$Chat tLRPC$Chat) {
        this.f$0 = chatEditTypeActivity;
        this.f$1 = tLRPC$Chat;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$loadAdminedChannels$11(this.f$1, dialogInterface, i);
    }
}
