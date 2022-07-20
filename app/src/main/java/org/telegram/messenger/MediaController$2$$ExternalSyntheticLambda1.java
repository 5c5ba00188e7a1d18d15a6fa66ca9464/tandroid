package org.telegram.messenger;

import java.nio.ByteBuffer;
import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MediaController.AnonymousClass2 f$0;
    public final /* synthetic */ ByteBuffer f$1;

    public /* synthetic */ MediaController$2$$ExternalSyntheticLambda1(MediaController.AnonymousClass2 anonymousClass2, ByteBuffer byteBuffer) {
        this.f$0 = anonymousClass2;
        this.f$1 = byteBuffer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}
