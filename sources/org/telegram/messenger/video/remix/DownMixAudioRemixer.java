package org.telegram.messenger.video.remix;

import java.nio.ShortBuffer;
import org.telegram.messenger.LiteMode;
/* loaded from: classes3.dex */
public class DownMixAudioRemixer implements AudioRemixer {
    public static short mix(short s, short s2) {
        int i = s + LiteMode.FLAG_CHAT_SCALE;
        int i2 = s2 + LiteMode.FLAG_CHAT_SCALE;
        int i3 = (i < 32768 || i2 < 32768) ? (i * i2) / LiteMode.FLAG_CHAT_SCALE : (((i + i2) * 2) - ((i * i2) / LiteMode.FLAG_CHAT_SCALE)) - 65535;
        return (short) ((i3 != 65536 ? i3 : 65535) - LiteMode.FLAG_CHAT_SCALE);
    }

    @Override // org.telegram.messenger.video.remix.AudioRemixer
    public int getRemixedSize(int i, int i2, int i3) {
        return i / 2;
    }

    @Override // org.telegram.messenger.video.remix.AudioRemixer
    public void remix(ShortBuffer shortBuffer, int i, ShortBuffer shortBuffer2, int i2) {
        int min = Math.min(shortBuffer.remaining() / 2, shortBuffer2.remaining());
        for (int i3 = 0; i3 < min; i3++) {
            shortBuffer2.put(mix(shortBuffer.get(), shortBuffer.get()));
        }
    }
}
