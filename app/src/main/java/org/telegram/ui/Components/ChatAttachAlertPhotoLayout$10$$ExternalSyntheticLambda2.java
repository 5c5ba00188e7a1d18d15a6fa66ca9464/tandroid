package org.telegram.ui.Components;

import java.io.File;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$10$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ChatAttachAlertPhotoLayout.AnonymousClass10 f$0;
    public final /* synthetic */ File f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ChatAttachAlertPhotoLayout$10$$ExternalSyntheticLambda2(ChatAttachAlertPhotoLayout.AnonymousClass10 anonymousClass10, File file, boolean z) {
        this.f$0 = anonymousClass10;
        this.f$1 = file;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$shutterReleased$3(this.f$1, this.f$2);
    }
}
