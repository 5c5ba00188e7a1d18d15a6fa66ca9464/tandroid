package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/* loaded from: classes3.dex */
public class MusicPlayerReceiver extends BroadcastReceiver {
    /* JADX WARN: Code restructure failed: missing block: B:35:0x004f, code lost:
    
        if (org.telegram.messenger.MediaController.getInstance().isMessagePaused() != false) goto L59;
     */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onReceive(Context context, Intent intent) {
        KeyEvent keyEvent;
        if (!intent.getAction().equals("android.intent.action.MEDIA_BUTTON")) {
            String action = intent.getAction();
            action.hashCode();
            switch (action) {
                case "org.telegram.android.musicplayer.close":
                    MediaController.getInstance().cleanupPlayer(true, true);
                    break;
                case "org.telegram.android.musicplayer.pause":
                case "android.media.AUDIO_BECOMING_NOISY":
                    MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
                    break;
                case "org.telegram.android.musicplayer.next":
                    MediaController.getInstance().playNextMessage();
                    break;
                case "org.telegram.android.musicplayer.play":
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                    break;
                case "org.telegram.android.musicplayer.previous":
                    MediaController.getInstance().playPreviousMessage();
                    break;
            }
            return;
        }
        if (intent.getExtras() == null || (keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT")) == null || keyEvent.getAction() != 0) {
            return;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != 79 && keyCode != 85) {
            if (keyCode != 87) {
                if (keyCode != 88) {
                    if (keyCode != 126) {
                        if (keyCode != 127) {
                            return;
                        }
                        MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
                        return;
                    }
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                    return;
                }
                MediaController.getInstance().playPreviousMessage();
                return;
            }
            MediaController.getInstance().playNextMessage();
        }
    }
}
