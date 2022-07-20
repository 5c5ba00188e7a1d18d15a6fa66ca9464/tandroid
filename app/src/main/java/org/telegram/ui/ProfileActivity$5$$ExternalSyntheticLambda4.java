package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$5$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ProfileActivity.AnonymousClass5 f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ ProfileActivity$5$$ExternalSyntheticLambda4(ProfileActivity.AnonymousClass5 anonymousClass5, TLRPC$User tLRPC$User) {
        this.f$0 = anonymousClass5;
        this.f$1 = tLRPC$User;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$2(this.f$1, dialogInterface, i);
    }
}
