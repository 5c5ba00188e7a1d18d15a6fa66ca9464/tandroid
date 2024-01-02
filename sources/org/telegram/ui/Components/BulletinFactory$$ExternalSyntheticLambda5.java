package org.telegram.ui.Components;

import org.telegram.messenger.SavedMessagesController;
/* loaded from: classes4.dex */
public final /* synthetic */ class BulletinFactory$$ExternalSyntheticLambda5 implements Runnable {
    public static final /* synthetic */ BulletinFactory$$ExternalSyntheticLambda5 INSTANCE = new BulletinFactory$$ExternalSyntheticLambda5();

    private /* synthetic */ BulletinFactory$$ExternalSyntheticLambda5() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        SavedMessagesController.openSavedMessages();
    }
}
