package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickerMasksAlert$$ExternalSyntheticLambda2 implements View.OnTouchListener {
    public final /* synthetic */ StickerMasksAlert f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ StickerMasksAlert$$ExternalSyntheticLambda2(StickerMasksAlert stickerMasksAlert, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = stickerMasksAlert;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$new$0;
        lambda$new$0 = this.f$0.lambda$new$0(this.f$1, view, motionEvent);
        return lambda$new$0;
    }
}
