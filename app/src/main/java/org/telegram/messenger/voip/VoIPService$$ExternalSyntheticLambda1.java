package org.telegram.messenger.voip;

import android.media.MediaPlayer;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda1 implements MediaPlayer.OnPreparedListener {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda1(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public final void onPrepared(MediaPlayer mediaPlayer) {
        this.f$0.lambda$startRingtoneAndVibration$63(mediaPlayer);
    }
}
