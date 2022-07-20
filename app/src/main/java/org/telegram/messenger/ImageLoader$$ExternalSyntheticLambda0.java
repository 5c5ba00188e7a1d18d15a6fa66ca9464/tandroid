package org.telegram.messenger;

import android.util.SparseArray;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SparseArray f$0;

    public /* synthetic */ ImageLoader$$ExternalSyntheticLambda0(SparseArray sparseArray) {
        this.f$0 = sparseArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        FileLoader.setMediaDirs(this.f$0);
    }
}
