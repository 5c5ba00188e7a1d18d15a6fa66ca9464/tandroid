package org.telegram.ui;

import android.net.Uri;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda236 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda236(ChatActivity chatActivity, Uri uri) {
        this.f$0 = chatActivity;
        this.f$1 = uri;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$onActivityResultFragment$115(this.f$1, z, i);
    }
}
