package org.telegram.ui;

import android.view.View;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda83 implements View.OnClickListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda83(ChatActivity chatActivity, int i, ArrayList arrayList) {
        this.f$0 = chatActivity;
        this.f$1 = i;
        this.f$2 = arrayList;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$createMenu$172(this.f$1, this.f$2, view);
    }
}
