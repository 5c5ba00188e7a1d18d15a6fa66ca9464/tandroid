package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ProfileGalleryView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileGalleryView$AvatarImageView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ProfileGalleryView.AvatarImageView f$0;

    public /* synthetic */ ProfileGalleryView$AvatarImageView$$ExternalSyntheticLambda0(ProfileGalleryView.AvatarImageView avatarImageView) {
        this.f$0 = avatarImageView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onDraw$0(valueAnimator);
    }
}
