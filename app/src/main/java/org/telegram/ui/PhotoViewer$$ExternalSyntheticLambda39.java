package org.telegram.ui;

import android.app.Activity;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda39 implements View.OnClickListener {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ Activity f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda39(PhotoViewer photoViewer, Activity activity) {
        this.f$0 = photoViewer;
        this.f$1 = activity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$setParentActivity$23(this.f$1, view);
    }
}
