package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class VideoCapturerDevice$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ VideoCapturerDevice f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ VideoCapturerDevice$$ExternalSyntheticLambda4(VideoCapturerDevice videoCapturerDevice, long j, int i) {
        this.f$0 = videoCapturerDevice;
        this.f$1 = j;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onStateChanged$7(this.f$1, this.f$2);
    }
}
