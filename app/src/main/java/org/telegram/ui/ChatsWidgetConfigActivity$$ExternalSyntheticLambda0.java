package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.EditWidgetActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatsWidgetConfigActivity$$ExternalSyntheticLambda0 implements EditWidgetActivity.EditWidgetActivityDelegate {
    public final /* synthetic */ ChatsWidgetConfigActivity f$0;

    public /* synthetic */ ChatsWidgetConfigActivity$$ExternalSyntheticLambda0(ChatsWidgetConfigActivity chatsWidgetConfigActivity) {
        this.f$0 = chatsWidgetConfigActivity;
    }

    @Override // org.telegram.ui.EditWidgetActivity.EditWidgetActivityDelegate
    public final void didSelectDialogs(ArrayList arrayList) {
        this.f$0.lambda$handleIntent$0(arrayList);
    }
}
