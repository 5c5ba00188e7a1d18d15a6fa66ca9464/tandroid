package org.telegram.ui;

import android.view.KeyEvent;
import android.view.View;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda3 implements View.OnKeyListener {
    public final /* synthetic */ PassportActivity.PhoneConfirmationView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda3(PassportActivity.PhoneConfirmationView phoneConfirmationView, int i) {
        this.f$0 = phoneConfirmationView;
        this.f$1 = i;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean lambda$setParams$4;
        lambda$setParams$4 = this.f$0.lambda$setParams$4(this.f$1, view, i, keyEvent);
        return lambda$setParams$4;
    }
}
