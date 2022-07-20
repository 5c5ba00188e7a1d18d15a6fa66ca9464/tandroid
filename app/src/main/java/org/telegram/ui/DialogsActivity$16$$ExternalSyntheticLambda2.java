package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$16$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DialogsActivity.AnonymousClass16 f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ DialogsActivity$16$$ExternalSyntheticLambda2(DialogsActivity.AnonymousClass16 anonymousClass16, long j) {
        this.f$0 = anonymousClass16;
        this.f$1 = j;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$needRemoveHint$0(this.f$1, dialogInterface, i);
    }
}
