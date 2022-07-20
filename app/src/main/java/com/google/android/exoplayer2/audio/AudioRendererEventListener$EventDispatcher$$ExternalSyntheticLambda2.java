package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
/* loaded from: classes.dex */
public final /* synthetic */ class AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AudioRendererEventListener.EventDispatcher f$0;
    public final /* synthetic */ Format f$1;

    public /* synthetic */ AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2(AudioRendererEventListener.EventDispatcher eventDispatcher, Format format) {
        this.f$0 = eventDispatcher;
        this.f$1 = format;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$inputFormatChanged$2(this.f$1);
    }
}
