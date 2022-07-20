package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$2$$ExternalSyntheticLambda3 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ WallpapersListActivity.AnonymousClass2 f$0;

    public /* synthetic */ WallpapersListActivity$2$$ExternalSyntheticLambda3(WallpapersListActivity.AnonymousClass2 anonymousClass2) {
        this.f$0 = anonymousClass2;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onItemClick$3(dialogsActivity, arrayList, charSequence, z);
    }
}
