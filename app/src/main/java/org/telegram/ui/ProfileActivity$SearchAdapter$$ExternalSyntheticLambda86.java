package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$SearchAdapter$$ExternalSyntheticLambda86 implements Runnable {
    public final /* synthetic */ ProfileActivity.SearchAdapter f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ ProfileActivity$SearchAdapter$$ExternalSyntheticLambda86(ProfileActivity.SearchAdapter searchAdapter, ArrayList arrayList) {
        this.f$0 = searchAdapter;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadFaqWebPage$85(this.f$1);
    }
}
