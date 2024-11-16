package org.telegram.ui;

/* loaded from: classes4.dex */
public final /* synthetic */ class CameraScanActivity$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ CameraScanActivity f$0;

    public /* synthetic */ CameraScanActivity$$ExternalSyntheticLambda10(CameraScanActivity cameraScanActivity) {
        this.f$0 = cameraScanActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.updateRecognized();
    }
}
