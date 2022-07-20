package org.telegram.messenger;

import android.content.Intent;
import org.telegram.messenger.NotificationBadge;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationBadge$DefaultBadger$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Intent f$0;

    public /* synthetic */ NotificationBadge$DefaultBadger$$ExternalSyntheticLambda0(Intent intent) {
        this.f$0 = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        NotificationBadge.DefaultBadger.lambda$executeBadge$0(this.f$0);
    }
}
