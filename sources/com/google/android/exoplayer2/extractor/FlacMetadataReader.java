package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.FlacStreamMetadata;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.flac.PictureFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public abstract class FlacMetadataReader {

    /* loaded from: classes.dex */
    public static final class FlacStreamMetadataHolder {
        public FlacStreamMetadata flacStreamMetadata;

        public FlacStreamMetadataHolder(FlacStreamMetadata flacStreamMetadata) {
            this.flacStreamMetadata = flacStreamMetadata;
        }
    }

    public static boolean checkAndPeekStreamMarker(ExtractorInput extractorInput) {
        ParsableByteArray parsableByteArray = new ParsableByteArray(4);
        extractorInput.peekFully(parsableByteArray.getData(), 0, 4);
        return parsableByteArray.readUnsignedInt() == 1716281667;
    }

    public static int getFrameStartMarker(ExtractorInput extractorInput) {
        extractorInput.resetPeekPosition();
        ParsableByteArray parsableByteArray = new ParsableByteArray(2);
        extractorInput.peekFully(parsableByteArray.getData(), 0, 2);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int i = readUnsignedShort >> 2;
        extractorInput.resetPeekPosition();
        if (i == 16382) {
            return readUnsignedShort;
        }
        throw ParserException.createForMalformedContainer("First frame does not start with sync code.", null);
    }

    public static Metadata peekId3Metadata(ExtractorInput extractorInput, boolean z) {
        Metadata peekId3Data = new Id3Peeker().peekId3Data(extractorInput, z ? null : Id3Decoder.NO_FRAMES_PREDICATE);
        if (peekId3Data == null || peekId3Data.length() == 0) {
            return null;
        }
        return peekId3Data;
    }

    public static Metadata readId3Metadata(ExtractorInput extractorInput, boolean z) {
        extractorInput.resetPeekPosition();
        long peekPosition = extractorInput.getPeekPosition();
        Metadata peekId3Metadata = peekId3Metadata(extractorInput, z);
        extractorInput.skipFully((int) (extractorInput.getPeekPosition() - peekPosition));
        return peekId3Metadata;
    }

    public static boolean readMetadataBlock(ExtractorInput extractorInput, FlacStreamMetadataHolder flacStreamMetadataHolder) {
        FlacStreamMetadata copyWithPictureFrames;
        extractorInput.resetPeekPosition();
        ParsableBitArray parsableBitArray = new ParsableBitArray(new byte[4]);
        extractorInput.peekFully(parsableBitArray.data, 0, 4);
        boolean readBit = parsableBitArray.readBit();
        int readBits = parsableBitArray.readBits(7);
        int readBits2 = parsableBitArray.readBits(24) + 4;
        if (readBits == 0) {
            copyWithPictureFrames = readStreamInfoBlock(extractorInput);
        } else {
            FlacStreamMetadata flacStreamMetadata = flacStreamMetadataHolder.flacStreamMetadata;
            if (flacStreamMetadata == null) {
                throw new IllegalArgumentException();
            }
            if (readBits == 3) {
                copyWithPictureFrames = flacStreamMetadata.copyWithSeekTable(readSeekTableMetadataBlock(extractorInput, readBits2));
            } else if (readBits == 4) {
                copyWithPictureFrames = flacStreamMetadata.copyWithVorbisComments(readVorbisCommentMetadataBlock(extractorInput, readBits2));
            } else {
                if (readBits != 6) {
                    extractorInput.skipFully(readBits2);
                    return readBit;
                }
                ParsableByteArray parsableByteArray = new ParsableByteArray(readBits2);
                extractorInput.readFully(parsableByteArray.getData(), 0, readBits2);
                parsableByteArray.skipBytes(4);
                copyWithPictureFrames = flacStreamMetadata.copyWithPictureFrames(ImmutableList.of((Object) PictureFrame.fromPictureBlock(parsableByteArray)));
            }
        }
        flacStreamMetadataHolder.flacStreamMetadata = copyWithPictureFrames;
        return readBit;
    }

    private static FlacStreamMetadata.SeekTable readSeekTableMetadataBlock(ExtractorInput extractorInput, int i) {
        ParsableByteArray parsableByteArray = new ParsableByteArray(i);
        extractorInput.readFully(parsableByteArray.getData(), 0, i);
        return readSeekTableMetadataBlock(parsableByteArray);
    }

    public static FlacStreamMetadata.SeekTable readSeekTableMetadataBlock(ParsableByteArray parsableByteArray) {
        parsableByteArray.skipBytes(1);
        int readUnsignedInt24 = parsableByteArray.readUnsignedInt24();
        long position = parsableByteArray.getPosition() + readUnsignedInt24;
        int i = readUnsignedInt24 / 18;
        long[] jArr = new long[i];
        long[] jArr2 = new long[i];
        int i2 = 0;
        while (true) {
            if (i2 >= i) {
                break;
            }
            long readLong = parsableByteArray.readLong();
            if (readLong == -1) {
                jArr = Arrays.copyOf(jArr, i2);
                jArr2 = Arrays.copyOf(jArr2, i2);
                break;
            }
            jArr[i2] = readLong;
            jArr2[i2] = parsableByteArray.readLong();
            parsableByteArray.skipBytes(2);
            i2++;
        }
        parsableByteArray.skipBytes((int) (position - parsableByteArray.getPosition()));
        return new FlacStreamMetadata.SeekTable(jArr, jArr2);
    }

    private static FlacStreamMetadata readStreamInfoBlock(ExtractorInput extractorInput) {
        byte[] bArr = new byte[38];
        extractorInput.readFully(bArr, 0, 38);
        return new FlacStreamMetadata(bArr, 4);
    }

    public static void readStreamMarker(ExtractorInput extractorInput) {
        ParsableByteArray parsableByteArray = new ParsableByteArray(4);
        extractorInput.readFully(parsableByteArray.getData(), 0, 4);
        if (parsableByteArray.readUnsignedInt() != 1716281667) {
            throw ParserException.createForMalformedContainer("Failed to read FLAC stream marker.", null);
        }
    }

    private static List readVorbisCommentMetadataBlock(ExtractorInput extractorInput, int i) {
        ParsableByteArray parsableByteArray = new ParsableByteArray(i);
        extractorInput.readFully(parsableByteArray.getData(), 0, i);
        parsableByteArray.skipBytes(4);
        return Arrays.asList(VorbisUtil.readVorbisCommentHeader(parsableByteArray, false, false).comments);
    }
}
