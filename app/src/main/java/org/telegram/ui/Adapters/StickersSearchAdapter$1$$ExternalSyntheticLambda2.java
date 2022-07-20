package org.telegram.ui.Adapters;

import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.MediaDataController;
import org.telegram.ui.Adapters.StickersSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersSearchAdapter$1$$ExternalSyntheticLambda2 implements MediaDataController.KeywordResultCallback {
    public final /* synthetic */ StickersSearchAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ HashMap f$2;

    public /* synthetic */ StickersSearchAdapter$1$$ExternalSyntheticLambda2(StickersSearchAdapter.AnonymousClass1 anonymousClass1, int i, HashMap hashMap) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = hashMap;
    }

    @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
    public final void run(ArrayList arrayList, String str) {
        this.f$0.lambda$run$0(this.f$1, this.f$2, arrayList, str);
    }
}
