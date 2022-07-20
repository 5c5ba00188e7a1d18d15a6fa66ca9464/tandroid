package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda54 implements AlertsCreator.BlockDialogCallback {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda54(DialogsActivity dialogsActivity, ArrayList arrayList) {
        this.f$0 = dialogsActivity;
        this.f$1 = arrayList;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.BlockDialogCallback
    public final void run(boolean z, boolean z2) {
        this.f$0.lambda$performSelectedDialogsAction$35(this.f$1, z, z2);
    }
}
