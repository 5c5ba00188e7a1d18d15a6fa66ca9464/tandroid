package org.telegram.messenger;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
/* loaded from: classes.dex */
public class HuaweiPushListenerService extends HmsMessageService {
    @Override // com.huawei.hms.push.HmsMessageService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Utilities.globalQueue.postRunnable(new HuaweiPushListenerService$$ExternalSyntheticLambda0(remoteMessage));
    }

    public static /* synthetic */ void lambda$onMessageReceived$0(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        String data = remoteMessage.getData();
        long sentTime = remoteMessage.getSentTime();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("HCM received data: " + data + " from: " + from);
        }
        PushListenerController.processRemoteMessage(13, data, sentTime);
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onNewToken(String str) {
        AndroidUtilities.runOnUIThread(new HuaweiPushListenerService$$ExternalSyntheticLambda1(str));
    }

    public static /* synthetic */ void lambda$onNewToken$1(String str) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Refreshed HCM token: " + str);
        }
        ApplicationLoader.postInitApplication();
        PushListenerController.sendRegistrationToServer(13, str);
    }
}
