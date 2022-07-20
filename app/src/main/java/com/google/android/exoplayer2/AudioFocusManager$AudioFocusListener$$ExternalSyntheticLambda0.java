package com.google.android.exoplayer2;

import com.google.android.exoplayer2.AudioFocusManager;
/* loaded from: classes.dex */
public final /* synthetic */ class AudioFocusManager$AudioFocusListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AudioFocusManager.AudioFocusListener f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ AudioFocusManager$AudioFocusListener$$ExternalSyntheticLambda0(AudioFocusManager.AudioFocusListener audioFocusListener, int i) {
        this.f$0 = audioFocusListener;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAudioFocusChange$0(this.f$1);
    }
}
