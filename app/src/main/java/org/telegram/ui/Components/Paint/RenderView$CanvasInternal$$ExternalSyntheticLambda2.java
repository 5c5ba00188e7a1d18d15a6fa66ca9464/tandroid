package org.telegram.ui.Components.Paint;

import org.telegram.ui.Components.Paint.RenderView;
/* loaded from: classes3.dex */
public final /* synthetic */ class RenderView$CanvasInternal$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ RenderView.CanvasInternal f$0;

    public /* synthetic */ RenderView$CanvasInternal$$ExternalSyntheticLambda2(RenderView.CanvasInternal canvasInternal) {
        this.f$0 = canvasInternal;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$scheduleRedraw$1();
    }
}
