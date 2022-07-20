package com.google.android.exoplayer2;

import com.google.android.exoplayer2.BasePlayer;
import com.google.android.exoplayer2.ExoPlayerImpl;
import com.google.android.exoplayer2.Player;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImpl$PlaybackInfoUpdate$$ExternalSyntheticLambda2 implements BasePlayer.ListenerInvocation {
    public final /* synthetic */ ExoPlayerImpl.PlaybackInfoUpdate f$0;

    public /* synthetic */ ExoPlayerImpl$PlaybackInfoUpdate$$ExternalSyntheticLambda2(ExoPlayerImpl.PlaybackInfoUpdate playbackInfoUpdate) {
        this.f$0 = playbackInfoUpdate;
    }

    @Override // com.google.android.exoplayer2.BasePlayer.ListenerInvocation
    public final void invokeListener(Player.EventListener eventListener) {
        this.f$0.lambda$run$4(eventListener);
    }
}
