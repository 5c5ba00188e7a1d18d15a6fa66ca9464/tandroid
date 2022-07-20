package com.google.firebase.messaging;

import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class ImageDownload$$Lambda$0 implements Callable {
    private final ImageDownload arg$1;

    public ImageDownload$$Lambda$0(ImageDownload imageDownload) {
        this.arg$1 = imageDownload;
    }

    @Override // java.util.concurrent.Callable
    public Object call() {
        return this.arg$1.blockingDownload();
    }
}
