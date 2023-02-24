package com.google.android.exoplayer2.source.rtsp.reader;

import android.util.Log;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.rtsp.RtpPacket;
import com.google.android.exoplayer2.source.rtsp.RtpPayloadFormat;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public final class RtpPcmReader implements RtpPayloadReader {
    private final RtpPayloadFormat payloadFormat;
    private TrackOutput trackOutput;
    private long firstReceivedTimestamp = -9223372036854775807L;
    private long startTimeOffsetUs = 0;
    private int previousSequenceNumber = -1;

    public RtpPcmReader(RtpPayloadFormat rtpPayloadFormat) {
        this.payloadFormat = rtpPayloadFormat;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void createTracks(ExtractorOutput extractorOutput, int i) {
        TrackOutput track = extractorOutput.track(i, 1);
        this.trackOutput = track;
        track.format(this.payloadFormat.format);
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void onReceivingFirstPacket(long j, int i) {
        this.firstReceivedTimestamp = j;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void consume(ParsableByteArray parsableByteArray, long j, int i, boolean z) {
        int nextSequenceNumber;
        Assertions.checkNotNull(this.trackOutput);
        int i2 = this.previousSequenceNumber;
        if (i2 != -1 && i != (nextSequenceNumber = RtpPacket.getNextSequenceNumber(i2))) {
            Log.w("RtpPcmReader", Util.formatInvariant("Received RTP packet with unexpected sequence number. Expected: %d; received: %d.", Integer.valueOf(nextSequenceNumber), Integer.valueOf(i)));
        }
        long sampleTimeUs = RtpReaderUtils.toSampleTimeUs(this.startTimeOffsetUs, j, this.firstReceivedTimestamp, this.payloadFormat.clockRate);
        int bytesLeft = parsableByteArray.bytesLeft();
        this.trackOutput.sampleData(parsableByteArray, bytesLeft);
        this.trackOutput.sampleMetadata(sampleTimeUs, 1, bytesLeft, 0, null);
        this.previousSequenceNumber = i;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void seek(long j, long j2) {
        this.firstReceivedTimestamp = j;
        this.startTimeOffsetUs = j2;
    }
}
