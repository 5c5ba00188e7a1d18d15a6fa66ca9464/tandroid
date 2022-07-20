package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ImageLoader.ArtworkLoadTask f$0;

    public /* synthetic */ ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1(ImageLoader.ArtworkLoadTask artworkLoadTask) {
        this.f$0 = artworkLoadTask;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCancelled$2();
    }
}
