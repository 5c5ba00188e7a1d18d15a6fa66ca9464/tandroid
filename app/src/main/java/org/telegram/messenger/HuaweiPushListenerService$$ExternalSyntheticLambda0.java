package org.telegram.messenger;

import com.huawei.hms.push.RemoteMessage;
/* loaded from: classes.dex */
public final /* synthetic */ class HuaweiPushListenerService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RemoteMessage f$0;

    public /* synthetic */ HuaweiPushListenerService$$ExternalSyntheticLambda0(RemoteMessage remoteMessage) {
        this.f$0 = remoteMessage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        HuaweiPushListenerService.lambda$onMessageReceived$0(this.f$0);
    }
}
