package com.google.android.exoplayer2.source.rtsp.reader;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.rtsp.RtpPacket;
import com.google.android.exoplayer2.source.rtsp.RtpPayloadFormat;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.common.primitives.Bytes;
/* loaded from: classes.dex */
final class RtpMpeg4Reader implements RtpPayloadReader {
    private int bufferFlags;
    private final RtpPayloadFormat payloadFormat;
    private int sampleLength;
    private long startTimeOffsetUs;
    private TrackOutput trackOutput;
    private long firstReceivedTimestamp = -9223372036854775807L;
    private int previousSequenceNumber = -1;

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void onReceivingFirstPacket(long j, int i) {
    }

    public RtpMpeg4Reader(RtpPayloadFormat rtpPayloadFormat) {
        this.payloadFormat = rtpPayloadFormat;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void createTracks(ExtractorOutput extractorOutput, int i) {
        TrackOutput track = extractorOutput.track(i, 2);
        this.trackOutput = track;
        ((TrackOutput) Util.castNonNull(track)).format(this.payloadFormat.format);
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void consume(ParsableByteArray parsableByteArray, long j, int i, boolean z) {
        int nextSequenceNumber;
        Assertions.checkStateNotNull(this.trackOutput);
        int i2 = this.previousSequenceNumber;
        if (i2 != -1 && i != (nextSequenceNumber = RtpPacket.getNextSequenceNumber(i2))) {
            Log.w("RtpMpeg4Reader", Util.formatInvariant("Received RTP packet with unexpected sequence number. Expected: %d; received: %d. Dropping packet.", Integer.valueOf(nextSequenceNumber), Integer.valueOf(i)));
        }
        int bytesLeft = parsableByteArray.bytesLeft();
        this.trackOutput.sampleData(parsableByteArray, bytesLeft);
        if (this.sampleLength == 0) {
            this.bufferFlags = getBufferFlagsFromVop(parsableByteArray);
        }
        this.sampleLength += bytesLeft;
        if (z) {
            if (this.firstReceivedTimestamp == -9223372036854775807L) {
                this.firstReceivedTimestamp = j;
            }
            this.trackOutput.sampleMetadata(RtpReaderUtils.toSampleTimeUs(this.startTimeOffsetUs, j, this.firstReceivedTimestamp, 90000), this.bufferFlags, this.sampleLength, 0, null);
            this.sampleLength = 0;
        }
        this.previousSequenceNumber = i;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void seek(long j, long j2) {
        this.firstReceivedTimestamp = j;
        this.startTimeOffsetUs = j2;
        this.sampleLength = 0;
    }

    private static int getBufferFlagsFromVop(ParsableByteArray parsableByteArray) {
        int indexOf = Bytes.indexOf(parsableByteArray.getData(), new byte[]{0, 0, 1, -74});
        if (indexOf != -1) {
            parsableByteArray.setPosition(indexOf + 4);
            return (parsableByteArray.peekUnsignedByte() >> 6) == 0 ? 1 : 0;
        }
        return 0;
    }
}
