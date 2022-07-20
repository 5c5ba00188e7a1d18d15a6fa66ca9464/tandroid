package org.telegram.ui;

import org.telegram.ui.NotificationsCustomSettingsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationsCustomSettingsActivity.SearchAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda0(NotificationsCustomSettingsActivity.SearchAdapter searchAdapter, String str) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$3(this.f$1);
    }
}
