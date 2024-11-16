package org.telegram.ui;

import org.telegram.messenger.Utilities;

/* loaded from: classes4.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda21 implements Utilities.Callback {
    public final /* synthetic */ LaunchActivity f$0;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda21(LaunchActivity launchActivity) {
        this.f$0 = launchActivity;
    }

    @Override // org.telegram.messenger.Utilities.Callback
    public final void run(Object obj) {
        this.f$0.onPowerSaver(((Boolean) obj).booleanValue());
    }
}
