package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayoutPreview;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView f$0;

    public /* synthetic */ ChatAttachAlertPhotoLayoutPreview$PreviewGroupsView$$ExternalSyntheticLambda1(ChatAttachAlertPhotoLayoutPreview.PreviewGroupsView previewGroupsView) {
        this.f$0 = previewGroupsView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startDragging$1(valueAnimator);
    }
}
