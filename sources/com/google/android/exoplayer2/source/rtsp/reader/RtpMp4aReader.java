package com.google.android.exoplayer2.source.rtsp.reader;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.rtsp.RtpPacket;
import com.google.android.exoplayer2.source.rtsp.RtpPayloadFormat;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableMap;
/* loaded from: classes.dex */
final class RtpMp4aReader implements RtpPayloadReader {
    private long firstReceivedTimestamp;
    private int fragmentedSampleSizeBytes;
    private long fragmentedSampleTimeUs;
    private final int numberOfSubframes;
    private final RtpPayloadFormat payloadFormat;
    private int previousSequenceNumber;
    private long startTimeOffsetUs;
    private TrackOutput trackOutput;

    public RtpMp4aReader(RtpPayloadFormat rtpPayloadFormat) {
        this.payloadFormat = rtpPayloadFormat;
        try {
            this.numberOfSubframes = getNumOfSubframesFromMpeg4AudioConfig(rtpPayloadFormat.fmtpParameters);
            this.firstReceivedTimestamp = -9223372036854775807L;
            this.previousSequenceNumber = -1;
            this.fragmentedSampleSizeBytes = 0;
            this.startTimeOffsetUs = 0L;
            this.fragmentedSampleTimeUs = -9223372036854775807L;
        } catch (ParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void createTracks(ExtractorOutput extractorOutput, int i) {
        TrackOutput track = extractorOutput.track(i, 2);
        this.trackOutput = track;
        ((TrackOutput) Util.castNonNull(track)).format(this.payloadFormat.format);
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void onReceivingFirstPacket(long j, int i) {
        Assertions.checkState(this.firstReceivedTimestamp == -9223372036854775807L);
        this.firstReceivedTimestamp = j;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void consume(ParsableByteArray parsableByteArray, long j, int i, boolean z) {
        Assertions.checkStateNotNull(this.trackOutput);
        int nextSequenceNumber = RtpPacket.getNextSequenceNumber(this.previousSequenceNumber);
        if (this.fragmentedSampleSizeBytes > 0 && nextSequenceNumber < i) {
            outputSampleMetadataForFragmentedPackets();
        }
        for (int i2 = 0; i2 < this.numberOfSubframes; i2++) {
            int i3 = 0;
            while (parsableByteArray.getPosition() < parsableByteArray.limit()) {
                int readUnsignedByte = parsableByteArray.readUnsignedByte();
                i3 += readUnsignedByte;
                if (readUnsignedByte != 255) {
                    break;
                }
            }
            this.trackOutput.sampleData(parsableByteArray, i3);
            this.fragmentedSampleSizeBytes += i3;
        }
        this.fragmentedSampleTimeUs = RtpReaderUtils.toSampleTimeUs(this.startTimeOffsetUs, j, this.firstReceivedTimestamp, this.payloadFormat.clockRate);
        if (z) {
            outputSampleMetadataForFragmentedPackets();
        }
        this.previousSequenceNumber = i;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void seek(long j, long j2) {
        this.firstReceivedTimestamp = j;
        this.fragmentedSampleSizeBytes = 0;
        this.startTimeOffsetUs = j2;
    }

    private static int getNumOfSubframesFromMpeg4AudioConfig(ImmutableMap<String, String> immutableMap) throws ParserException {
        String str = immutableMap.get("config");
        int i = 0;
        i = 0;
        if (str != null && str.length() % 2 == 0) {
            ParsableBitArray parsableBitArray = new ParsableBitArray(Util.getBytesFromHexString(str));
            int readBits = parsableBitArray.readBits(1);
            if (readBits == 0) {
                Assertions.checkArgument(parsableBitArray.readBits(1) == 1, "Only supports allStreamsSameTimeFraming.");
                int readBits2 = parsableBitArray.readBits(6);
                Assertions.checkArgument(parsableBitArray.readBits(4) == 0, "Only suppors one program.");
                Assertions.checkArgument(parsableBitArray.readBits(3) == 0, "Only suppors one layer.");
                i = readBits2;
            } else {
                throw ParserException.createForMalformedDataOfUnknownType("unsupported audio mux version: " + readBits, null);
            }
        }
        return i + 1;
    }

    private void outputSampleMetadataForFragmentedPackets() {
        ((TrackOutput) Assertions.checkNotNull(this.trackOutput)).sampleMetadata(this.fragmentedSampleTimeUs, 1, this.fragmentedSampleSizeBytes, 0, null);
        this.fragmentedSampleSizeBytes = 0;
        this.fragmentedSampleTimeUs = -9223372036854775807L;
    }
}
