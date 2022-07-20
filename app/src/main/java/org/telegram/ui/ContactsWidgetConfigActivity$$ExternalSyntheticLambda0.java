package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.EditWidgetActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContactsWidgetConfigActivity$$ExternalSyntheticLambda0 implements EditWidgetActivity.EditWidgetActivityDelegate {
    public final /* synthetic */ ContactsWidgetConfigActivity f$0;

    public /* synthetic */ ContactsWidgetConfigActivity$$ExternalSyntheticLambda0(ContactsWidgetConfigActivity contactsWidgetConfigActivity) {
        this.f$0 = contactsWidgetConfigActivity;
    }

    @Override // org.telegram.ui.EditWidgetActivity.EditWidgetActivityDelegate
    public final void didSelectDialogs(ArrayList arrayList) {
        this.f$0.lambda$handleIntent$0(arrayList);
    }
}
