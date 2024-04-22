package org.telegram.ui;

import org.telegram.ui.Stories.StealthModeAlert;
/* loaded from: classes4.dex */
public final /* synthetic */ class DialogsActivity$29$$ExternalSyntheticLambda12 implements Runnable {
    public static final /* synthetic */ DialogsActivity$29$$ExternalSyntheticLambda12 INSTANCE = new DialogsActivity$29$$ExternalSyntheticLambda12();

    private /* synthetic */ DialogsActivity$29$$ExternalSyntheticLambda12() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        StealthModeAlert.showStealthModeEnabledBulletin();
    }
}
