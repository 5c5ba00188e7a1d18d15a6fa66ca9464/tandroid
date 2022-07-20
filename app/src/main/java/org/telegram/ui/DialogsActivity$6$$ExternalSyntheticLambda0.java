package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$6$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DialogsActivity.AnonymousClass6 f$0;
    public final /* synthetic */ MessagesController.DialogFilter f$1;

    public /* synthetic */ DialogsActivity$6$$ExternalSyntheticLambda0(DialogsActivity.AnonymousClass6 anonymousClass6, MessagesController.DialogFilter dialogFilter) {
        this.f$0 = anonymousClass6;
        this.f$1 = dialogFilter;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showDeleteAlert$2(this.f$1, dialogInterface, i);
    }
}
