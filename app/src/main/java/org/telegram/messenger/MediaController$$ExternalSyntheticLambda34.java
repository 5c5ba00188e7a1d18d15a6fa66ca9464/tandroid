package org.telegram.messenger;

import java.io.File;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda34 implements Runnable {
    public final /* synthetic */ MessageObject f$0;
    public final /* synthetic */ File f$1;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda34(MessageObject messageObject, File file) {
        this.f$0 = messageObject;
        this.f$1 = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaController.lambda$playMessage$21(this.f$0, this.f$1);
    }
}
