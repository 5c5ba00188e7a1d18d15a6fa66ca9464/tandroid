package org.telegram.messenger.voip;

import android.media.AudioManager;
import org.telegram.messenger.voip.VoIPService;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AudioManager f$0;

    public /* synthetic */ VoIPService$1$$ExternalSyntheticLambda0(AudioManager audioManager) {
        this.f$0 = audioManager;
    }

    @Override // java.lang.Runnable
    public final void run() {
        VoIPService.AnonymousClass1.lambda$run$1(this.f$0);
    }
}
