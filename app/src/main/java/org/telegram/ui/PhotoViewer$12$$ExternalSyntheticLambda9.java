package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$12$$ExternalSyntheticLambda9 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ PhotoViewer.AnonymousClass12 f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ChatActivity f$2;

    public /* synthetic */ PhotoViewer$12$$ExternalSyntheticLambda9(PhotoViewer.AnonymousClass12 anonymousClass12, ArrayList arrayList, ChatActivity chatActivity) {
        this.f$0 = anonymousClass12;
        this.f$1 = arrayList;
        this.f$2 = chatActivity;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onItemClick$4(this.f$1, this.f$2, dialogsActivity, arrayList, charSequence, z);
    }
}
