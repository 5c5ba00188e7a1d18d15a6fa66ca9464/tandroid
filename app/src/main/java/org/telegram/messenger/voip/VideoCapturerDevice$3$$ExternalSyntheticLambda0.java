package org.telegram.messenger.voip;

import org.telegram.messenger.voip.VideoCapturerDevice;
/* loaded from: classes.dex */
public final /* synthetic */ class VideoCapturerDevice$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ VideoCapturerDevice$3$$ExternalSyntheticLambda0(boolean z) {
        this.f$0 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        VideoCapturerDevice.AnonymousClass3.lambda$onCameraSwitchDone$0(this.f$0);
    }
}
