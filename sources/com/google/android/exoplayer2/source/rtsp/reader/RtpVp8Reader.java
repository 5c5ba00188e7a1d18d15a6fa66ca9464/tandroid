package com.google.android.exoplayer2.source.rtsp.reader;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.rtsp.RtpPacket;
import com.google.android.exoplayer2.source.rtsp.RtpPayloadFormat;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
final class RtpVp8Reader implements RtpPayloadReader {
    private boolean gotFirstPacketOfVp8Frame;
    private boolean isKeyFrame;
    private boolean isOutputFormatSet;
    private final RtpPayloadFormat payloadFormat;
    private TrackOutput trackOutput;
    private long firstReceivedTimestamp = -9223372036854775807L;
    private int previousSequenceNumber = -1;
    private int fragmentedSampleSizeBytes = -1;
    private long fragmentedSampleTimeUs = -9223372036854775807L;
    private long startTimeOffsetUs = 0;

    public RtpVp8Reader(RtpPayloadFormat rtpPayloadFormat) {
        this.payloadFormat = rtpPayloadFormat;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void createTracks(ExtractorOutput extractorOutput, int i) {
        TrackOutput track = extractorOutput.track(i, 2);
        this.trackOutput = track;
        track.format(this.payloadFormat.format);
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void onReceivingFirstPacket(long j, int i) {
        Assertions.checkState(this.firstReceivedTimestamp == -9223372036854775807L);
        this.firstReceivedTimestamp = j;
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void consume(ParsableByteArray parsableByteArray, long j, int i, boolean z) {
        Assertions.checkStateNotNull(this.trackOutput);
        if (validateVp8Descriptor(parsableByteArray, i)) {
            if (this.fragmentedSampleSizeBytes == -1 && this.gotFirstPacketOfVp8Frame) {
                this.isKeyFrame = (parsableByteArray.peekUnsignedByte() & 1) == 0;
            }
            if (!this.isOutputFormatSet) {
                int position = parsableByteArray.getPosition();
                parsableByteArray.setPosition(position + 6);
                int readLittleEndianUnsignedShort = parsableByteArray.readLittleEndianUnsignedShort() & 16383;
                int readLittleEndianUnsignedShort2 = parsableByteArray.readLittleEndianUnsignedShort() & 16383;
                parsableByteArray.setPosition(position);
                Format format = this.payloadFormat.format;
                if (readLittleEndianUnsignedShort != format.width || readLittleEndianUnsignedShort2 != format.height) {
                    this.trackOutput.format(format.buildUpon().setWidth(readLittleEndianUnsignedShort).setHeight(readLittleEndianUnsignedShort2).build());
                }
                this.isOutputFormatSet = true;
            }
            int bytesLeft = parsableByteArray.bytesLeft();
            this.trackOutput.sampleData(parsableByteArray, bytesLeft);
            int i2 = this.fragmentedSampleSizeBytes;
            if (i2 == -1) {
                this.fragmentedSampleSizeBytes = bytesLeft;
            } else {
                this.fragmentedSampleSizeBytes = i2 + bytesLeft;
            }
            this.fragmentedSampleTimeUs = RtpReaderUtils.toSampleTimeUs(this.startTimeOffsetUs, j, this.firstReceivedTimestamp, 90000);
            if (z) {
                outputSampleMetadataForFragmentedPackets();
            }
            this.previousSequenceNumber = i;
        }
    }

    @Override // com.google.android.exoplayer2.source.rtsp.reader.RtpPayloadReader
    public void seek(long j, long j2) {
        this.firstReceivedTimestamp = j;
        this.fragmentedSampleSizeBytes = -1;
        this.startTimeOffsetUs = j2;
    }

    private boolean validateVp8Descriptor(ParsableByteArray parsableByteArray, int i) {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        if ((readUnsignedByte & 16) == 16 && (readUnsignedByte & 7) == 0) {
            if (this.gotFirstPacketOfVp8Frame && this.fragmentedSampleSizeBytes > 0) {
                outputSampleMetadataForFragmentedPackets();
            }
            this.gotFirstPacketOfVp8Frame = true;
        } else if (this.gotFirstPacketOfVp8Frame) {
            int nextSequenceNumber = RtpPacket.getNextSequenceNumber(this.previousSequenceNumber);
            if (i < nextSequenceNumber) {
                Log.w("RtpVP8Reader", Util.formatInvariant("Received RTP packet with unexpected sequence number. Expected: %d; received: %d. Dropping packet.", Integer.valueOf(nextSequenceNumber), Integer.valueOf(i)));
                return false;
            }
        } else {
            Log.w("RtpVP8Reader", "RTP packet is not the start of a new VP8 partition, skipping.");
            return false;
        }
        if ((readUnsignedByte & 128) != 0) {
            int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
            if ((readUnsignedByte2 & 128) != 0 && (parsableByteArray.readUnsignedByte() & 128) != 0) {
                parsableByteArray.skipBytes(1);
            }
            if ((readUnsignedByte2 & 64) != 0) {
                parsableByteArray.skipBytes(1);
            }
            if ((readUnsignedByte2 & 32) != 0 || (readUnsignedByte2 & 16) != 0) {
                parsableByteArray.skipBytes(1);
            }
        }
        return true;
    }

    private void outputSampleMetadataForFragmentedPackets() {
        long j = this.fragmentedSampleTimeUs;
        boolean z = this.isKeyFrame;
        ((TrackOutput) Assertions.checkNotNull(this.trackOutput)).sampleMetadata(j, z ? 1 : 0, this.fragmentedSampleSizeBytes, 0, null);
        this.fragmentedSampleSizeBytes = -1;
        this.fragmentedSampleTimeUs = -9223372036854775807L;
        this.gotFirstPacketOfVp8Frame = false;
    }
}
