package com.googlecode.mp4parser.h264.read;

import com.googlecode.mp4parser.h264.Debug;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class CAVLCReader extends BitstreamReader {
    public CAVLCReader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public long readNBit(int i, String str) throws IOException {
        long readNBit = readNBit(i);
        trace(str, String.valueOf(readNBit));
        return readNBit;
    }

    private int readUE() throws IOException {
        int i = 0;
        while (read1Bit() == 0) {
            i++;
        }
        if (i > 0) {
            return (int) (((1 << i) - 1) + readNBit(i));
        }
        return 0;
    }

    public int readUE(String str) throws IOException {
        int readUE = readUE();
        trace(str, String.valueOf(readUE));
        return readUE;
    }

    public boolean readBool(String str) throws IOException {
        boolean z = read1Bit() != 0;
        trace(str, z ? "1" : "0");
        return z;
    }

    public int readU(int i, String str) throws IOException {
        return (int) readNBit(i, str);
    }

    private void trace(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        String valueOf = String.valueOf(BitstreamReader.bitsRead - this.debugBits.length());
        int length = 8 - valueOf.length();
        sb.append("@" + valueOf);
        for (int i = 0; i < length; i++) {
            sb.append(' ');
        }
        sb.append(str);
        int length2 = (100 - sb.length()) - this.debugBits.length();
        for (int i2 = 0; i2 < length2; i2++) {
            sb.append(' ');
        }
        sb.append(this.debugBits);
        sb.append(" (" + str2 + ")");
        this.debugBits.clear();
        Debug.println(sb.toString());
    }
}
