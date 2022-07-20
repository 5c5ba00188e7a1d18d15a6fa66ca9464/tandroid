package com.google.android.exoplayer2.util;

import java.util.concurrent.ThreadFactory;
/* loaded from: classes.dex */
public final /* synthetic */ class Util$$ExternalSyntheticLambda0 implements ThreadFactory {
    public final /* synthetic */ String f$0;

    public /* synthetic */ Util$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        Thread lambda$newSingleThreadExecutor$0;
        lambda$newSingleThreadExecutor$0 = Util.lambda$newSingleThreadExecutor$0(this.f$0, runnable);
        return lambda$newSingleThreadExecutor$0;
    }
}
