package org.telegram.ui;

import org.telegram.messenger.voip.NativeInstance;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda49 implements NativeInstance.AudioLevelsCallback {
    public final /* synthetic */ GroupCallActivity f$0;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda49(GroupCallActivity groupCallActivity) {
        this.f$0 = groupCallActivity;
    }

    @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
    public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
        this.f$0.lambda$new$10(iArr, fArr, zArr);
    }
}
