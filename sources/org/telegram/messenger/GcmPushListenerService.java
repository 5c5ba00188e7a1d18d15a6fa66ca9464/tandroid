package org.telegram.messenger;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

/* loaded from: classes3.dex */
public class GcmPushListenerService extends FirebaseMessagingService {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onNewToken$0(String str) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Refreshed FCM token: " + str);
        }
        ApplicationLoader.postInitApplication();
        PushListenerController.sendRegistrationToServer(2, str);
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        long sentTime = remoteMessage.getSentTime();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("FCM received data: " + data + " from: " + from);
        }
        PushListenerController.processRemoteMessage(2, (String) data.get("p"), sentTime);
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(final String str) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GcmPushListenerService.lambda$onNewToken$0(str);
            }
        });
    }
}
