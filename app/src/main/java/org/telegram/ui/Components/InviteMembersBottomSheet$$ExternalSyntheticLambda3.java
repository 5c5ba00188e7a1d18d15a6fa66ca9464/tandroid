package org.telegram.ui.Components;

import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ EditTextBoldCursor f$0;

    public /* synthetic */ InviteMembersBottomSheet$$ExternalSyntheticLambda3(EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextBoldCursor;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AndroidUtilities.showKeyboard(this.f$0);
    }
}
