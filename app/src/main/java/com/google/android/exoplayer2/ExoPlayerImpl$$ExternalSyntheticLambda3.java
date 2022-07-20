package com.google.android.exoplayer2;

import com.google.android.exoplayer2.BasePlayer;
import com.google.android.exoplayer2.Player;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImpl$$ExternalSyntheticLambda3 implements BasePlayer.ListenerInvocation {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ ExoPlayerImpl$$ExternalSyntheticLambda3(boolean z, boolean z2, int i, boolean z3, int i2, boolean z4, boolean z5) {
        this.f$0 = z;
        this.f$1 = z2;
        this.f$2 = i;
        this.f$3 = z3;
        this.f$4 = i2;
        this.f$5 = z4;
        this.f$6 = z5;
    }

    @Override // com.google.android.exoplayer2.BasePlayer.ListenerInvocation
    public final void invokeListener(Player.EventListener eventListener) {
        ExoPlayerImpl.lambda$setPlayWhenReady$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, eventListener);
    }
}
