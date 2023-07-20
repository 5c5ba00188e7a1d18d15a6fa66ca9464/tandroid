package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.util.ListenerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImpl$$ExternalSyntheticLambda22 implements ListenerSet.Event {
    public static final /* synthetic */ ExoPlayerImpl$$ExternalSyntheticLambda22 INSTANCE = new ExoPlayerImpl$$ExternalSyntheticLambda22();

    private /* synthetic */ ExoPlayerImpl$$ExternalSyntheticLambda22() {
    }

    @Override // com.google.android.exoplayer2.util.ListenerSet.Event
    public final void invoke(Object obj) {
        ((Player.Listener) obj).onSeekProcessed();
    }
}
