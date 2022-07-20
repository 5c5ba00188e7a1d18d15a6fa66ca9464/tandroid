package org.telegram.messenger;

import android.view.View;
/* loaded from: classes.dex */
public final /* synthetic */ class AndroidUtilities$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda4(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.run();
    }
}
