package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class OggPacket {
    private boolean populated;
    private int segmentCount;
    private final OggPageHeader pageHeader = new OggPageHeader();
    private final ParsableByteArray packetArray = new ParsableByteArray(new byte[65025], 0);
    private int currentSegmentIndex = -1;

    public void reset() {
        this.pageHeader.reset();
        this.packetArray.reset(0);
        this.currentSegmentIndex = -1;
        this.populated = false;
    }

    public boolean populate(ExtractorInput extractorInput) throws IOException {
        int i;
        Assertions.checkState(extractorInput != null);
        if (this.populated) {
            this.populated = false;
            this.packetArray.reset(0);
        }
        while (!this.populated) {
            if (this.currentSegmentIndex < 0) {
                if (!this.pageHeader.skipToNextPage(extractorInput) || !this.pageHeader.populate(extractorInput, true)) {
                    return false;
                }
                OggPageHeader oggPageHeader = this.pageHeader;
                int i2 = oggPageHeader.headerSize;
                if ((oggPageHeader.type & 1) == 1 && this.packetArray.limit() == 0) {
                    i2 += calculatePacketSize(0);
                    i = this.segmentCount + 0;
                } else {
                    i = 0;
                }
                if (!ExtractorUtil.skipFullyQuietly(extractorInput, i2)) {
                    return false;
                }
                this.currentSegmentIndex = i;
            }
            int calculatePacketSize = calculatePacketSize(this.currentSegmentIndex);
            int i3 = this.currentSegmentIndex + this.segmentCount;
            if (calculatePacketSize > 0) {
                ParsableByteArray parsableByteArray = this.packetArray;
                parsableByteArray.ensureCapacity(parsableByteArray.limit() + calculatePacketSize);
                if (!ExtractorUtil.readFullyQuietly(extractorInput, this.packetArray.getData(), this.packetArray.limit(), calculatePacketSize)) {
                    return false;
                }
                ParsableByteArray parsableByteArray2 = this.packetArray;
                parsableByteArray2.setLimit(parsableByteArray2.limit() + calculatePacketSize);
                this.populated = this.pageHeader.laces[i3 + (-1)] != 255;
            }
            if (i3 == this.pageHeader.pageSegmentCount) {
                i3 = -1;
            }
            this.currentSegmentIndex = i3;
        }
        return true;
    }

    public OggPageHeader getPageHeader() {
        return this.pageHeader;
    }

    public ParsableByteArray getPayload() {
        return this.packetArray;
    }

    public void trimPayload() {
        if (this.packetArray.getData().length == 65025) {
            return;
        }
        ParsableByteArray parsableByteArray = this.packetArray;
        parsableByteArray.reset(Arrays.copyOf(parsableByteArray.getData(), Math.max(65025, this.packetArray.limit())), this.packetArray.limit());
    }

    private int calculatePacketSize(int i) {
        int i2;
        int i3 = 0;
        this.segmentCount = 0;
        do {
            int i4 = this.segmentCount;
            int i5 = i + i4;
            OggPageHeader oggPageHeader = this.pageHeader;
            if (i5 >= oggPageHeader.pageSegmentCount) {
                break;
            }
            int[] iArr = oggPageHeader.laces;
            this.segmentCount = i4 + 1;
            i2 = iArr[i4 + i];
            i3 += i2;
        } while (i2 == 255);
        return i3;
    }
}
