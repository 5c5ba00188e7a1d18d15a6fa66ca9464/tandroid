package org.telegram.messenger.video.audio_input;

import java.nio.ShortBuffer;
import org.telegram.messenger.video.AudioBufferConverter;
import org.telegram.messenger.video.AudioConversions;
import org.telegram.messenger.video.AudioDecoder;

/* loaded from: classes3.dex */
public class GeneralAudioInput extends AudioInput {
    private AudioBufferConverter audioBufferConverter;
    private ShortBuffer buffer;
    private final AudioDecoder decoder;
    private boolean hasRemaining;
    private int outputChannelCount;
    private int outputSampleRate;
    private int requiredShortsForStartOffset;
    private int startOffsetShortsCounter;
    private long startOffsetUs;

    public GeneralAudioInput(String str) {
        this.decoder = new AudioDecoder(str);
        init();
    }

    public GeneralAudioInput(String str, int i) {
        this.decoder = new AudioDecoder(str, i);
        init();
    }

    private void decode() {
        ShortBuffer shortBuffer = this.buffer;
        if (shortBuffer == null || shortBuffer.remaining() <= 0) {
            AudioDecoder.DecodedBufferData decode = this.decoder.decode();
            if (decode.index < 0) {
                this.buffer = null;
                return;
            }
            this.buffer = this.audioBufferConverter.convert(decode.byteBuffer.asShortBuffer(), this.decoder.getSampleRate(), this.decoder.getChannelCount(), this.outputSampleRate, this.outputChannelCount);
            this.decoder.releaseOutputBuffer(decode.index);
        }
    }

    private void init() {
        this.audioBufferConverter = new AudioBufferConverter();
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public short getNext() {
        if (!hasRemaining()) {
            throw new RuntimeException("Audio input has no remaining value.");
        }
        int i = this.startOffsetShortsCounter;
        if (i < this.requiredShortsForStartOffset) {
            this.startOffsetShortsCounter = i + 1;
            return (short) 0;
        }
        decode();
        ShortBuffer shortBuffer = this.buffer;
        short s = (shortBuffer == null || shortBuffer.remaining() <= 0) ? (short) 0 : this.buffer.get();
        decode();
        ShortBuffer shortBuffer2 = this.buffer;
        if (shortBuffer2 == null || shortBuffer2.remaining() < 1) {
            this.hasRemaining = false;
        }
        return s;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public int getSampleRate() {
        return this.decoder.getSampleRate();
    }

    public long getStartOffsetUs() {
        return this.startOffsetUs;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public boolean hasRemaining() {
        return this.hasRemaining;
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public void release() {
        this.buffer = null;
        this.hasRemaining = false;
        this.decoder.stop();
        this.decoder.release();
    }

    public void setEndTimeUs(long j) {
        this.decoder.setEndTimeUs(j);
    }

    public void setStartOffsetUs(long j) {
        if (j < 0) {
            j = 0;
        }
        this.startOffsetUs = j;
    }

    public void setStartTimeUs(long j) {
        this.decoder.setStartTimeUs(j);
    }

    @Override // org.telegram.messenger.video.audio_input.AudioInput
    public void start(int i, int i2) {
        this.outputSampleRate = i;
        this.outputChannelCount = i2;
        this.hasRemaining = true;
        this.decoder.start();
        this.requiredShortsForStartOffset = AudioConversions.usToShorts(getStartOffsetUs(), this.outputSampleRate, this.outputChannelCount);
        this.startOffsetShortsCounter = 0;
    }
}
