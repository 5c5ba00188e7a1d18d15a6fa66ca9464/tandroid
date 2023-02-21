package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;
import org.telegram.messenger.LiteMode;
/* loaded from: classes.dex */
public class BitWriterBuffer {
    private ByteBuffer buffer;
    int initialPos;
    int position = 0;

    public BitWriterBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        this.initialPos = byteBuffer.position();
    }

    public void writeBits(int i, int i2) {
        int i3 = this.position;
        int i4 = 8 - (i3 % 8);
        if (i2 <= i4) {
            int i5 = this.buffer.get(this.initialPos + (i3 / 8));
            if (i5 < 0) {
                i5 += LiteMode.FLAG_CHAT_BLUR;
            }
            int i6 = i5 + (i << (i4 - i2));
            ByteBuffer byteBuffer = this.buffer;
            int i7 = this.initialPos + (this.position / 8);
            if (i6 > 127) {
                i6 -= 256;
            }
            byteBuffer.put(i7, (byte) i6);
            this.position += i2;
        } else {
            int i8 = i2 - i4;
            writeBits(i >> i8, i4);
            writeBits(i & ((1 << i8) - 1), i8);
        }
        ByteBuffer byteBuffer2 = this.buffer;
        int i9 = this.initialPos;
        int i10 = this.position;
        byteBuffer2.position(i9 + (i10 / 8) + (i10 % 8 <= 0 ? 0 : 1));
    }
}
