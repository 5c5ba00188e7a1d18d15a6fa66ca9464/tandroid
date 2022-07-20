package org.telegram.ui.Components.voip;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallMiniTextureView$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ GroupCallMiniTextureView f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ GroupCallMiniTextureView$$ExternalSyntheticLambda7(GroupCallMiniTextureView groupCallMiniTextureView, View view) {
        this.f$0 = groupCallMiniTextureView;
        this.f$1 = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateAttachState$2(this.f$1);
    }
}
