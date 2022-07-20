package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$3$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity.AnonymousClass3 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PassportActivity$3$$ExternalSyntheticLambda0(PassportActivity.AnonymousClass3 anonymousClass3, int i) {
        this.f$0 = anonymousClass3;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onIdentityDone$1(this.f$1, dialogInterface, i);
    }
}
