package com.google.android.exoplayer2;

import com.google.android.exoplayer2.BasePlayer;
import com.google.android.exoplayer2.Player;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImpl$$ExternalSyntheticLambda1 implements BasePlayer.ListenerInvocation {
    public final /* synthetic */ PlaybackParameters f$0;

    public /* synthetic */ ExoPlayerImpl$$ExternalSyntheticLambda1(PlaybackParameters playbackParameters) {
        this.f$0 = playbackParameters;
    }

    @Override // com.google.android.exoplayer2.BasePlayer.ListenerInvocation
    public final void invokeListener(Player.EventListener eventListener) {
        eventListener.onPlaybackParametersChanged(this.f$0);
    }
}
