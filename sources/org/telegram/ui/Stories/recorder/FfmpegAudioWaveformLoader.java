package org.telegram.ui.Stories.recorder;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
/* loaded from: classes4.dex */
public class FfmpegAudioWaveformLoader {
    private Utilities.Callback2<short[], Integer> onChunkReceived;
    private volatile boolean running = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: init */
    public native void lambda$new$0(String str, int i);

    public FfmpegAudioWaveformLoader(final String str, final int i, Utilities.Callback2<short[], Integer> callback2) {
        this.onChunkReceived = callback2;
        Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.recorder.FfmpegAudioWaveformLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FfmpegAudioWaveformLoader.this.lambda$new$0(str, i);
            }
        });
    }

    private void receiveChunk(final short[] sArr, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.FfmpegAudioWaveformLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FfmpegAudioWaveformLoader.this.lambda$receiveChunk$1(sArr, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$receiveChunk$1(short[] sArr, int i) {
        this.onChunkReceived.run(sArr, Integer.valueOf(i));
    }

    public void destroy() {
        Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.recorder.FfmpegAudioWaveformLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FfmpegAudioWaveformLoader.this.lambda$destroy$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroy$2() {
        this.running = false;
    }
}
