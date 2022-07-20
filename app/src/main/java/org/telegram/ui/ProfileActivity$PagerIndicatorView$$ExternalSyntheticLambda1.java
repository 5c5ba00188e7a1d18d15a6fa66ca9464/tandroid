package org.telegram.ui;

import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ProfileActivity.PagerIndicatorView f$0;

    public /* synthetic */ ProfileActivity$PagerIndicatorView$$ExternalSyntheticLambda1(ProfileActivity.PagerIndicatorView pagerIndicatorView) {
        this.f$0 = pagerIndicatorView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.updateAvatarItemsInternal();
    }
}
