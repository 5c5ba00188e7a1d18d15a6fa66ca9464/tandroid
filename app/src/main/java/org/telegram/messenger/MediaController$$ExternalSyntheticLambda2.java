package org.telegram.messenger;

import android.media.AudioManager;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda2 implements AudioManager.OnAudioFocusChangeListener {
    public final /* synthetic */ MediaController f$0;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda2(MediaController mediaController) {
        this.f$0 = mediaController;
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public final void onAudioFocusChange(int i) {
        this.f$0.lambda$new$0(i);
    }
}
