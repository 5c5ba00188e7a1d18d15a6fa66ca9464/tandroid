package org.telegram.ui.Components;

import androidx.arch.core.util.Function;
/* loaded from: classes3.dex */
public final /* synthetic */ class ShareAlert$$ExternalSyntheticLambda9 implements Function {
    public final /* synthetic */ ShareAlert f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ShareAlert$$ExternalSyntheticLambda9(ShareAlert shareAlert, boolean z) {
        this.f$0 = shareAlert;
        this.f$1 = z;
    }

    @Override // androidx.arch.core.util.Function
    public final Object apply(Object obj) {
        Bulletin lambda$copyLink$15;
        lambda$copyLink$15 = this.f$0.lambda$copyLink$15(this.f$1, (BulletinFactory) obj);
        return lambda$copyLink$15;
    }
}
