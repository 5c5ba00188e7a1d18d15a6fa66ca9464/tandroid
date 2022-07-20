package org.telegram.messenger;

import android.net.Uri;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationsController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ Uri f$0;

    public /* synthetic */ NotificationsController$$ExternalSyntheticLambda4(Uri uri) {
        this.f$0 = uri;
    }

    @Override // java.lang.Runnable
    public final void run() {
        NotificationsController.lambda$showExtraNotifications$34(this.f$0);
    }
}
