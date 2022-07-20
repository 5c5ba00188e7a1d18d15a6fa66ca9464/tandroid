package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda50 implements MessagesStorage.IntCallback {
    public final /* synthetic */ DialogsActivity f$0;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda50(DialogsActivity dialogsActivity) {
        this.f$0 = dialogsActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$askForPermissons$42(i);
    }
}
