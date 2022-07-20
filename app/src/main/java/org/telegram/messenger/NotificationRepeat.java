package org.telegram.messenger;

import android.app.IntentService;
import android.content.Intent;
/* loaded from: classes.dex */
public class NotificationRepeat extends IntentService {
    public NotificationRepeat() {
        super("NotificationRepeat");
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        int intExtra = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
        if (!UserConfig.isValidAccount(intExtra)) {
            return;
        }
        AndroidUtilities.runOnUIThread(new NotificationRepeat$$ExternalSyntheticLambda0(intExtra));
    }

    public static /* synthetic */ void lambda$onHandleIntent$0(int i) {
        NotificationsController.getInstance(i).repeatNotificationMaybe();
    }
}
