package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda75 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ ArrayList f$4;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda75(MediaDataController mediaDataController, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
        this.f$0 = mediaDataController;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = arrayList3;
        this.f$4 = arrayList4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadHints$104(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
