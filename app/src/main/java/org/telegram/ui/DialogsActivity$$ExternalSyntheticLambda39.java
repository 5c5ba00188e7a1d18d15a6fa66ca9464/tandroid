package org.telegram.ui;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda39 implements Runnable {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda39(DialogsActivity dialogsActivity, int i, ArrayList arrayList) {
        this.f$0 = dialogsActivity;
        this.f$1 = i;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$performSelectedDialogsAction$33(this.f$1, this.f$2);
    }
}
