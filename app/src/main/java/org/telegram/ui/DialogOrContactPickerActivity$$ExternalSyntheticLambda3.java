package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogOrContactPickerActivity$$ExternalSyntheticLambda3 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ DialogOrContactPickerActivity f$0;

    public /* synthetic */ DialogOrContactPickerActivity$$ExternalSyntheticLambda3(DialogOrContactPickerActivity dialogOrContactPickerActivity) {
        this.f$0 = dialogOrContactPickerActivity;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$new$1(dialogsActivity, arrayList, charSequence, z);
    }
}
