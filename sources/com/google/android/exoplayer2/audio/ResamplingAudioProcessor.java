package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class ResamplingAudioProcessor extends BaseAudioProcessor {
    @Override // com.google.android.exoplayer2.audio.BaseAudioProcessor
    public AudioProcessor.AudioFormat onConfigure(AudioProcessor.AudioFormat audioFormat) {
        int i = audioFormat.encoding;
        if (i == 3 || i == 2 || i == 268435456 || i == 536870912 || i == 805306368 || i == 4) {
            return i != 2 ? new AudioProcessor.AudioFormat(audioFormat.sampleRate, audioFormat.channelCount, 2) : AudioProcessor.AudioFormat.NOT_SET;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(audioFormat);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00af A[ADDED_TO_REGION, LOOP:4: B:33:0x00af->B:34:0x00b1, LOOP_START, PHI: r0
      0x00af: PHI (r0v1 int) = (r0v0 int), (r0v2 int) binds: [B:12:0x0036, B:34:0x00b1] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void queueInput(ByteBuffer byteBuffer) {
        int i;
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i2 = limit - position;
        int i3 = this.inputAudioFormat.encoding;
        if (i3 != 3) {
            if (i3 != 4) {
                if (i3 != 268435456) {
                    if (i3 == 536870912) {
                        i2 /= 3;
                    } else if (i3 != 805306368) {
                        throw new IllegalStateException();
                    }
                }
                ByteBuffer replaceOutputBuffer = replaceOutputBuffer(i2);
                i = this.inputAudioFormat.encoding;
                if (i == 3) {
                    while (position < limit) {
                        replaceOutputBuffer.put((byte) 0);
                        replaceOutputBuffer.put((byte) ((byteBuffer.get(position) & 255) - 128));
                        position++;
                    }
                } else if (i == 4) {
                    while (position < limit) {
                        short constrainValue = (short) (Util.constrainValue(byteBuffer.getFloat(position), -1.0f, 1.0f) * 32767.0f);
                        replaceOutputBuffer.put((byte) (constrainValue & 255));
                        replaceOutputBuffer.put((byte) ((constrainValue >> 8) & NotificationCenter.playerDidStartPlaying));
                        position += 4;
                    }
                } else if (i == 268435456) {
                    while (position < limit) {
                        replaceOutputBuffer.put(byteBuffer.get(position + 1));
                        replaceOutputBuffer.put(byteBuffer.get(position));
                        position += 2;
                    }
                } else if (i == 536870912) {
                    while (position < limit) {
                        replaceOutputBuffer.put(byteBuffer.get(position + 1));
                        replaceOutputBuffer.put(byteBuffer.get(position + 2));
                        position += 3;
                    }
                } else {
                    if (i != 805306368) {
                        throw new IllegalStateException();
                    }
                    while (position < limit) {
                        replaceOutputBuffer.put(byteBuffer.get(position + 2));
                        replaceOutputBuffer.put(byteBuffer.get(position + 3));
                        position += 4;
                    }
                }
                byteBuffer.position(byteBuffer.limit());
                replaceOutputBuffer.flip();
            }
            i2 /= 2;
            ByteBuffer replaceOutputBuffer2 = replaceOutputBuffer(i2);
            i = this.inputAudioFormat.encoding;
            if (i == 3) {
            }
            byteBuffer.position(byteBuffer.limit());
            replaceOutputBuffer2.flip();
        }
        i2 *= 2;
        ByteBuffer replaceOutputBuffer22 = replaceOutputBuffer(i2);
        i = this.inputAudioFormat.encoding;
        if (i == 3) {
        }
        byteBuffer.position(byteBuffer.limit());
        replaceOutputBuffer22.flip();
    }
}
