package org.telegram.messenger.video.remix;

import java.nio.ShortBuffer;
/* loaded from: classes.dex */
public class PassThroughAudioRemixer implements AudioRemixer {
    @Override // org.telegram.messenger.video.remix.AudioRemixer
    public int getRemixedSize(int i, int i2, int i3) {
        return i;
    }

    @Override // org.telegram.messenger.video.remix.AudioRemixer
    public void remix(ShortBuffer shortBuffer, int i, ShortBuffer shortBuffer2, int i2) {
        shortBuffer2.put(shortBuffer);
    }
}
