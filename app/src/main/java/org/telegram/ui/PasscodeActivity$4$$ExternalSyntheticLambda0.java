package org.telegram.ui;

import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.PasscodeActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PasscodeActivity$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PasscodeActivity.AnonymousClass4 f$0;
    public final /* synthetic */ ActionBarMenuSubItem f$1;

    public /* synthetic */ PasscodeActivity$4$$ExternalSyntheticLambda0(PasscodeActivity.AnonymousClass4 anonymousClass4, ActionBarMenuSubItem actionBarMenuSubItem) {
        this.f$0 = anonymousClass4;
        this.f$1 = actionBarMenuSubItem;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onItemClick$0(this.f$1);
    }
}
