package org.telegram.ui.Components.spoilers;

import android.graphics.Bitmap;
/* loaded from: classes3.dex */
public final /* synthetic */ class SpoilerEffectBitmapFactory$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SpoilerEffectBitmapFactory f$0;
    public final /* synthetic */ Bitmap f$1;

    public /* synthetic */ SpoilerEffectBitmapFactory$$ExternalSyntheticLambda1(SpoilerEffectBitmapFactory spoilerEffectBitmapFactory, Bitmap bitmap) {
        this.f$0 = spoilerEffectBitmapFactory;
        this.f$1 = bitmap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkUpdate$0(this.f$1);
    }
}
