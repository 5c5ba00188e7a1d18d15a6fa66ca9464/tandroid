package org.telegram.ui;

import java.io.File;
import org.telegram.ui.SecretMediaViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class SecretMediaViewer$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SecretMediaViewer.AnonymousClass1 f$0;
    public final /* synthetic */ File f$1;

    public /* synthetic */ SecretMediaViewer$1$$ExternalSyntheticLambda0(SecretMediaViewer.AnonymousClass1 anonymousClass1, File file) {
        this.f$0 = anonymousClass1;
        this.f$1 = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onError$0(this.f$1);
    }
}
