package org.telegram.messenger;

import androidx.collection.LongSparseArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda50 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ LongSparseArray f$1;
    public final /* synthetic */ LongSparseArray f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ Runnable f$5;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda50(MediaDataController mediaDataController, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, long j, Runnable runnable) {
        this.f$0 = mediaDataController;
        this.f$1 = longSparseArray;
        this.f$2 = longSparseArray2;
        this.f$3 = z;
        this.f$4 = j;
        this.f$5 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadReplyMessagesForMessages$139(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
