package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
/* loaded from: classes3.dex */
public final /* synthetic */ class ShareAlert$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ ShareAlert f$0;
    public final /* synthetic */ ActionBarMenuSubItem f$1;
    public final /* synthetic */ ActionBarMenuSubItem f$2;

    public /* synthetic */ ShareAlert$$ExternalSyntheticLambda5(ShareAlert shareAlert, ActionBarMenuSubItem actionBarMenuSubItem, ActionBarMenuSubItem actionBarMenuSubItem2) {
        this.f$0 = shareAlert;
        this.f$1 = actionBarMenuSubItem;
        this.f$2 = actionBarMenuSubItem2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onSendLongClick$11(this.f$1, this.f$2, view);
    }
}
