package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ GroupCallActivity.AnonymousClass6 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ GroupCallActivity$6$$ExternalSyntheticLambda4(GroupCallActivity.AnonymousClass6 anonymousClass6, boolean z) {
        this.f$0 = anonymousClass6;
        this.f$1 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$2(this.f$1, dialogInterface, i);
    }
}
