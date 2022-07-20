package org.telegram.ui.Components.voip;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallRenderersContainer$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ GroupCallRenderersContainer f$0;
    public final /* synthetic */ GroupCallMiniTextureView f$1;

    public /* synthetic */ GroupCallRenderersContainer$$ExternalSyntheticLambda8(GroupCallRenderersContainer groupCallRenderersContainer, GroupCallMiniTextureView groupCallMiniTextureView) {
        this.f$0 = groupCallRenderersContainer;
        this.f$1 = groupCallMiniTextureView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$requestFullscreen$4(this.f$1);
    }
}
