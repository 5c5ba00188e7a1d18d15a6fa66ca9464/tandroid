package org.telegram.ui;

import android.content.Intent;
import org.telegram.messenger.ContactsLoadingObserver;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda64 implements ContactsLoadingObserver.Callback {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda64(LaunchActivity launchActivity, Intent intent) {
        this.f$0 = launchActivity;
        this.f$1 = intent;
    }

    @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
    public final void onResult(boolean z) {
        this.f$0.lambda$handleIntent$10(this.f$1, z);
    }
}
