package org.telegram.ui;

import android.animation.AnimatorSet;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContactsActivity$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ContactsActivity f$0;
    public final /* synthetic */ AnimatorSet f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ View f$3;

    public /* synthetic */ ContactsActivity$$ExternalSyntheticLambda5(ContactsActivity contactsActivity, AnimatorSet animatorSet, boolean z, View view) {
        this.f$0 = contactsActivity;
        this.f$1 = animatorSet;
        this.f$2 = z;
        this.f$3 = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCustomTransitionAnimation$8(this.f$1, this.f$2, this.f$3);
    }
}
