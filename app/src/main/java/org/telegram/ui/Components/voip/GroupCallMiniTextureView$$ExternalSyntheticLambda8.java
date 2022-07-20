package org.telegram.ui.Components.voip;

import android.graphics.Bitmap;
import org.webrtc.GlGenericDrawer;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallMiniTextureView$$ExternalSyntheticLambda8 implements GlGenericDrawer.TextureCallback {
    public final /* synthetic */ GroupCallMiniTextureView f$0;

    public /* synthetic */ GroupCallMiniTextureView$$ExternalSyntheticLambda8(GroupCallMiniTextureView groupCallMiniTextureView) {
        this.f$0 = groupCallMiniTextureView;
    }

    @Override // org.webrtc.GlGenericDrawer.TextureCallback
    public final void run(Bitmap bitmap, int i) {
        this.f$0.lambda$saveThumb$5(bitmap, i);
    }
}
