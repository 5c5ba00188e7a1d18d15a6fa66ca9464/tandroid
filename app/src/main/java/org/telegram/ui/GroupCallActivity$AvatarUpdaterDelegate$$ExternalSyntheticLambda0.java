package org.telegram.ui;

import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ GroupCallActivity.AvatarUpdaterDelegate f$0;

    public /* synthetic */ GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda0(GroupCallActivity.AvatarUpdaterDelegate avatarUpdaterDelegate) {
        this.f$0 = avatarUpdaterDelegate;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didUploadPhoto$2();
    }
}
