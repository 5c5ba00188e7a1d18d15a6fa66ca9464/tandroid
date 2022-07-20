package org.telegram.ui.ActionBar;

import android.view.ViewTreeObserver;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertDialog$1$$ExternalSyntheticLambda0 implements ViewTreeObserver.OnScrollChangedListener {
    public final /* synthetic */ AlertDialog.AnonymousClass1 f$0;

    public /* synthetic */ AlertDialog$1$$ExternalSyntheticLambda0(AlertDialog.AnonymousClass1 anonymousClass1) {
        this.f$0 = anonymousClass1;
    }

    @Override // android.view.ViewTreeObserver.OnScrollChangedListener
    public final void onScrollChanged() {
        this.f$0.lambda$onLayout$1();
    }
}
