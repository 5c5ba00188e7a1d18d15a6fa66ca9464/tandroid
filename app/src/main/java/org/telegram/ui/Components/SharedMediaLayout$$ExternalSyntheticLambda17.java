package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$$ExternalSyntheticLambda17 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ SharedMediaLayout f$0;

    public /* synthetic */ SharedMediaLayout$$ExternalSyntheticLambda17(SharedMediaLayout sharedMediaLayout) {
        this.f$0 = sharedMediaLayout;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onActionBarItemClick$16(dialogsActivity, arrayList, charSequence, z);
    }
}
