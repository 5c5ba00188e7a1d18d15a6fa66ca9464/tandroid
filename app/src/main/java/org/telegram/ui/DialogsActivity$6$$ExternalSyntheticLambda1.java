package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$6$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ DialogsActivity.AnonymousClass6 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ MessagesController.DialogFilter f$3;

    public /* synthetic */ DialogsActivity$6$$ExternalSyntheticLambda1(DialogsActivity.AnonymousClass6 anonymousClass6, int i, int i2, MessagesController.DialogFilter dialogFilter) {
        this.f$0 = anonymousClass6;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = dialogFilter;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$didSelectTab$4(this.f$1, this.f$2, this.f$3, view);
    }
}
