package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.microsoft.appcenter.distribute.DistributeConstants;
import java.io.IOException;
/* loaded from: classes3.dex */
final class Sniffer {
    private static final int ID_EBML = 440786851;
    private static final int SEARCH_LENGTH = 1024;
    private int peekLength;
    private final ParsableByteArray scratch = new ParsableByteArray(8);

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a9, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        long inputLength = input.getLength();
        long j = DistributeConstants.KIBIBYTE_IN_BYTES;
        if (inputLength != -1 && inputLength <= DistributeConstants.KIBIBYTE_IN_BYTES) {
            j = inputLength;
        }
        int bytesToSearch = (int) j;
        input.peekFully(this.scratch.data, 0, 4);
        long tag = this.scratch.readUnsignedInt();
        this.peekLength = 4;
        while (tag != 440786851) {
            int i = this.peekLength + 1;
            this.peekLength = i;
            if (i == bytesToSearch) {
                return false;
            }
            input.peekFully(this.scratch.data, 0, 1);
            tag = ((tag << 8) & (-256)) | (this.scratch.data[0] & 255);
        }
        long headerSize = readUint(input);
        long headerStart = this.peekLength;
        if (headerSize != Long.MIN_VALUE && (inputLength == -1 || headerStart + headerSize < inputLength)) {
            while (true) {
                int i2 = this.peekLength;
                int bytesToSearch2 = bytesToSearch;
                if (i2 >= headerStart + headerSize) {
                    return ((long) i2) == headerStart + headerSize;
                }
                long id = readUint(input);
                if (id == Long.MIN_VALUE) {
                    return false;
                }
                long tag2 = tag;
                long size = readUint(input);
                if (size < 0 || size > 2147483647L) {
                    break;
                }
                if (size != 0) {
                    int sizeInt = (int) size;
                    input.advancePeekPosition(sizeInt);
                    this.peekLength += sizeInt;
                }
                bytesToSearch = bytesToSearch2;
                tag = tag2;
            }
        }
        return false;
    }

    private long readUint(ExtractorInput input) throws IOException, InterruptedException {
        input.peekFully(this.scratch.data, 0, 1);
        int value = this.scratch.data[0] & 255;
        if (value == 0) {
            return Long.MIN_VALUE;
        }
        int mask = 128;
        int length = 0;
        while ((value & mask) == 0) {
            mask >>= 1;
            length++;
        }
        int value2 = value & (mask ^ (-1));
        input.peekFully(this.scratch.data, 1, length);
        for (int i = 0; i < length; i++) {
            value2 = (value2 << 8) + (this.scratch.data[i + 1] & 255);
        }
        int i2 = this.peekLength;
        this.peekLength = i2 + length + 1;
        return value2;
    }
}
