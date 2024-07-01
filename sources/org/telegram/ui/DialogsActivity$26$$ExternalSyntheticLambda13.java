package org.telegram.ui;

import org.telegram.ui.Stories.StealthModeAlert;
/* loaded from: classes4.dex */
public final /* synthetic */ class DialogsActivity$26$$ExternalSyntheticLambda13 implements Runnable {
    public static final /* synthetic */ DialogsActivity$26$$ExternalSyntheticLambda13 INSTANCE = new DialogsActivity$26$$ExternalSyntheticLambda13();

    private /* synthetic */ DialogsActivity$26$$ExternalSyntheticLambda13() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        StealthModeAlert.showStealthModeEnabledBulletin();
    }
}
