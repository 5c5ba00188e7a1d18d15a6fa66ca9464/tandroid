package org.telegram.messenger.video.audio_input;

import org.telegram.messenger.video.AudioConversions;
/* loaded from: classes3.dex */
public class BlankAudioInput extends AudioInput {
    public final long durationUs;
    private int remainingShorts;
    private int requiredShortsForDuration;

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public int getSampleRate() {
        return -1;
    }

    public BlankAudioInput(long j) {
        this.durationUs = j;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public boolean hasRemaining() {
        return this.remainingShorts > 0;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public void start(int i, int i2) {
        int usToShorts = AudioConversions.usToShorts(this.durationUs, i, i2);
        this.requiredShortsForDuration = usToShorts;
        this.remainingShorts = usToShorts;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public short getNext() {
        if (!hasRemaining()) {
            throw new RuntimeException("Audio input has no remaining value.");
        }
        this.remainingShorts--;
        if (isLoopingEnabled() && this.remainingShorts == 0) {
            this.remainingShorts = this.requiredShortsForDuration;
            return (short) 0;
        }
        return (short) 0;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public void release() {
        this.remainingShorts = 0;
    }
}
