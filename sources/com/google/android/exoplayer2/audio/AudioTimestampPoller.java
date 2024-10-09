package com.google.android.exoplayer2.audio;

import android.media.AudioTimestamp;
import android.media.AudioTrack;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
final class AudioTimestampPoller {
    private final AudioTimestampV19 audioTimestamp;
    private long initialTimestampPositionFrames;
    private long initializeSystemTimeUs;
    private long lastTimestampSampleTimeUs;
    private long sampleIntervalUs;
    private int state;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class AudioTimestampV19 {
        private final AudioTimestamp audioTimestamp = new AudioTimestamp();
        private final AudioTrack audioTrack;
        private long lastTimestampPositionFrames;
        private long lastTimestampRawPositionFrames;
        private long rawTimestampFramePositionWrapCount;

        public AudioTimestampV19(AudioTrack audioTrack) {
            this.audioTrack = audioTrack;
        }

        public long getTimestampPositionFrames() {
            return this.lastTimestampPositionFrames;
        }

        public long getTimestampSystemTimeUs() {
            return this.audioTimestamp.nanoTime / 1000;
        }

        public boolean maybeUpdateTimestamp() {
            boolean timestamp = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (timestamp) {
                long j = this.audioTimestamp.framePosition;
                if (this.lastTimestampRawPositionFrames > j) {
                    this.rawTimestampFramePositionWrapCount++;
                }
                this.lastTimestampRawPositionFrames = j;
                this.lastTimestampPositionFrames = j + (this.rawTimestampFramePositionWrapCount << 32);
            }
            return timestamp;
        }
    }

    public AudioTimestampPoller(AudioTrack audioTrack) {
        if (Util.SDK_INT >= 19) {
            this.audioTimestamp = new AudioTimestampV19(audioTrack);
            reset();
        } else {
            this.audioTimestamp = null;
            updateState(3);
        }
    }

    private void updateState(int i) {
        this.state = i;
        long j = 10000;
        if (i == 0) {
            this.lastTimestampSampleTimeUs = 0L;
            this.initialTimestampPositionFrames = -1L;
            this.initializeSystemTimeUs = System.nanoTime() / 1000;
        } else if (i != 1) {
            if (i == 2 || i == 3) {
                j = 10000000;
            } else {
                if (i != 4) {
                    throw new IllegalStateException();
                }
                j = 500000;
            }
        }
        this.sampleIntervalUs = j;
    }

    public void acceptTimestamp() {
        if (this.state == 4) {
            reset();
        }
    }

    public long getTimestampPositionFrames() {
        AudioTimestampV19 audioTimestampV19 = this.audioTimestamp;
        if (audioTimestampV19 != null) {
            return audioTimestampV19.getTimestampPositionFrames();
        }
        return -1L;
    }

    public long getTimestampSystemTimeUs() {
        AudioTimestampV19 audioTimestampV19 = this.audioTimestamp;
        if (audioTimestampV19 != null) {
            return audioTimestampV19.getTimestampSystemTimeUs();
        }
        return -9223372036854775807L;
    }

    public boolean hasAdvancingTimestamp() {
        return this.state == 2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x002d, code lost:
    
        if (r0 != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0030, code lost:
    
        if (r0 == false) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean maybePollTimestamp(long j) {
        AudioTimestampV19 audioTimestampV19 = this.audioTimestamp;
        if (audioTimestampV19 == null || j - this.lastTimestampSampleTimeUs < this.sampleIntervalUs) {
            return false;
        }
        this.lastTimestampSampleTimeUs = j;
        boolean maybeUpdateTimestamp = audioTimestampV19.maybeUpdateTimestamp();
        int i = this.state;
        if (i != 0) {
            if (i == 1) {
                if (maybeUpdateTimestamp) {
                    if (this.audioTimestamp.getTimestampPositionFrames() > this.initialTimestampPositionFrames) {
                        updateState(2);
                    }
                }
                reset();
            } else if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        throw new IllegalStateException();
                    }
                }
            }
        } else if (maybeUpdateTimestamp) {
            if (this.audioTimestamp.getTimestampSystemTimeUs() < this.initializeSystemTimeUs) {
                return false;
            }
            this.initialTimestampPositionFrames = this.audioTimestamp.getTimestampPositionFrames();
            updateState(1);
        } else if (j - this.initializeSystemTimeUs > 500000) {
            updateState(3);
        }
        return maybeUpdateTimestamp;
    }

    public void rejectTimestamp() {
        updateState(4);
    }

    public void reset() {
        if (this.audioTimestamp != null) {
            updateState(0);
        }
    }
}
