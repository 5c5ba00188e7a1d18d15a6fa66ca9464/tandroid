package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertDocumentLayout$SearchAdapter$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ChatAttachAlertDocumentLayout.SearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ ChatAttachAlertDocumentLayout$SearchAdapter$$ExternalSyntheticLambda3(ChatAttachAlertDocumentLayout.SearchAdapter searchAdapter, String str, boolean z, ArrayList arrayList) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
        this.f$2 = z;
        this.f$3 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$search$0(this.f$1, this.f$2, this.f$3);
    }
}
