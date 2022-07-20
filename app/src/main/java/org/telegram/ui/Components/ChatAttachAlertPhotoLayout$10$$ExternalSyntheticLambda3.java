package org.telegram.ui.Components;

import org.telegram.messenger.camera.CameraController;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$10$$ExternalSyntheticLambda3 implements CameraController.VideoTakeCallback {
    public final /* synthetic */ ChatAttachAlertPhotoLayout.AnonymousClass10 f$0;

    public /* synthetic */ ChatAttachAlertPhotoLayout$10$$ExternalSyntheticLambda3(ChatAttachAlertPhotoLayout.AnonymousClass10 anonymousClass10) {
        this.f$0 = anonymousClass10;
    }

    @Override // org.telegram.messenger.camera.CameraController.VideoTakeCallback
    public final void onFinishVideoRecording(String str, long j) {
        this.f$0.lambda$shutterLongPressed$1(str, j);
    }
}
