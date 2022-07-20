package org.telegram.ui;

import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsCustomSettingsActivity$$ExternalSyntheticLambda11 implements ProfileNotificationsActivity.ProfileNotificationsActivityDelegate {
    public final /* synthetic */ NotificationsCustomSettingsActivity f$0;

    public /* synthetic */ NotificationsCustomSettingsActivity$$ExternalSyntheticLambda11(NotificationsCustomSettingsActivity notificationsCustomSettingsActivity) {
        this.f$0 = notificationsCustomSettingsActivity;
    }

    @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
    public final void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException) {
        this.f$0.lambda$createView$0(notificationException);
    }

    @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
    public /* synthetic */ void didRemoveException(long j) {
        ProfileNotificationsActivity.ProfileNotificationsActivityDelegate.CC.$default$didRemoveException(this, j);
    }
}
