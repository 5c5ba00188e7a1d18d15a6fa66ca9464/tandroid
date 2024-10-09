package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.FlacStreamMetadata;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class FlacDecoderJni {
    private static final int TEMP_BUFFER_SIZE = 8192;
    private ByteBuffer byteBufferData;
    private boolean endOfExtractorInput;
    private ExtractorInput extractorInput;
    private final long nativeDecoderContext;
    private byte[] tempBuffer;

    /* loaded from: classes.dex */
    public static final class FlacFrameDecodeException extends Exception {
        public final int errorCode;

        public FlacFrameDecodeException(String str, int i) {
            super(str);
            this.errorCode = i;
        }
    }

    public FlacDecoderJni() {
        if (!FlacLibrary.isAvailable()) {
            throw new FlacDecoderException("Failed to load decoder native libraries.");
        }
        long flacInit = flacInit();
        this.nativeDecoderContext = flacInit;
        if (flacInit == 0) {
            throw new FlacDecoderException("Failed to initialize decoder");
        }
    }

    private native FlacStreamMetadata flacDecodeMetadata(long j);

    private native int flacDecodeToArray(long j, byte[] bArr);

    private native int flacDecodeToBuffer(long j, ByteBuffer byteBuffer);

    private native void flacFlush(long j);

    private native long flacGetDecodePosition(long j);

    private native long flacGetLastFrameFirstSampleIndex(long j);

    private native long flacGetLastFrameTimestamp(long j);

    private native long flacGetNextFrameFirstSampleIndex(long j);

    private native boolean flacGetSeekPoints(long j, long j2, long[] jArr);

    private native String flacGetStateString(long j);

    private native long flacInit();

    private native boolean flacIsDecoderAtEndOfStream(long j);

    private native void flacRelease(long j);

    private native void flacReset(long j, long j2);

    private int readFromExtractorInput(ExtractorInput extractorInput, byte[] bArr, int i, int i2) {
        int read = extractorInput.read(bArr, i, i2);
        if (read != -1) {
            return read;
        }
        this.endOfExtractorInput = true;
        return 0;
    }

    public void clearData() {
        this.byteBufferData = null;
        this.extractorInput = null;
    }

    public void decodeSample(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        int flacDecodeToBuffer = byteBuffer.isDirect() ? flacDecodeToBuffer(this.nativeDecoderContext, byteBuffer) : flacDecodeToArray(this.nativeDecoderContext, byteBuffer.array());
        if (flacDecodeToBuffer < 0) {
            if (!isDecoderAtEndOfInput()) {
                throw new FlacFrameDecodeException("Cannot decode FLAC frame", flacDecodeToBuffer);
            }
            flacDecodeToBuffer = 0;
        }
        byteBuffer.limit(flacDecodeToBuffer);
    }

    public void decodeSampleWithBacktrackPosition(ByteBuffer byteBuffer, long j) {
        try {
            decodeSample(byteBuffer);
        } catch (IOException e) {
            if (j >= 0) {
                reset(j);
                ExtractorInput extractorInput = this.extractorInput;
                if (extractorInput != null) {
                    extractorInput.setRetryPosition(j, e);
                }
            }
            throw e;
        }
    }

    public FlacStreamMetadata decodeStreamMetadata() {
        FlacStreamMetadata flacDecodeMetadata = flacDecodeMetadata(this.nativeDecoderContext);
        if (flacDecodeMetadata != null) {
            return flacDecodeMetadata;
        }
        throw ParserException.createForMalformedContainer("Failed to decode stream metadata", null);
    }

    public void flush() {
        flacFlush(this.nativeDecoderContext);
    }

    public long getDecodePosition() {
        return flacGetDecodePosition(this.nativeDecoderContext);
    }

    public long getLastFrameFirstSampleIndex() {
        return flacGetLastFrameFirstSampleIndex(this.nativeDecoderContext);
    }

    public long getLastFrameTimestamp() {
        return flacGetLastFrameTimestamp(this.nativeDecoderContext);
    }

    public long getNextFrameFirstSampleIndex() {
        return flacGetNextFrameFirstSampleIndex(this.nativeDecoderContext);
    }

    public SeekMap.SeekPoints getSeekPoints(long j) {
        long[] jArr = new long[4];
        if (!flacGetSeekPoints(this.nativeDecoderContext, j, jArr)) {
            return null;
        }
        SeekPoint seekPoint = new SeekPoint(jArr[0], jArr[1]);
        return new SeekMap.SeekPoints(seekPoint, jArr[2] == jArr[0] ? seekPoint : new SeekPoint(jArr[2], jArr[3]));
    }

    public String getStateString() {
        return flacGetStateString(this.nativeDecoderContext);
    }

    public boolean isDecoderAtEndOfInput() {
        return flacIsDecoderAtEndOfStream(this.nativeDecoderContext);
    }

    public boolean isEndOfData() {
        ByteBuffer byteBuffer = this.byteBufferData;
        if (byteBuffer != null) {
            return byteBuffer.remaining() == 0;
        }
        if (this.extractorInput != null) {
            return this.endOfExtractorInput;
        }
        return true;
    }

    public int read(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();
        ByteBuffer byteBuffer2 = this.byteBufferData;
        if (byteBuffer2 != null) {
            int min = Math.min(remaining, byteBuffer2.remaining());
            int limit = this.byteBufferData.limit();
            ByteBuffer byteBuffer3 = this.byteBufferData;
            byteBuffer3.limit(byteBuffer3.position() + min);
            byteBuffer.put(this.byteBufferData);
            this.byteBufferData.limit(limit);
            return min;
        }
        ExtractorInput extractorInput = this.extractorInput;
        if (extractorInput == null) {
            return -1;
        }
        byte[] bArr = (byte[]) Util.castNonNull(this.tempBuffer);
        int min2 = Math.min(remaining, 8192);
        int readFromExtractorInput = readFromExtractorInput(extractorInput, bArr, 0, min2);
        if (readFromExtractorInput < 4) {
            readFromExtractorInput += readFromExtractorInput(extractorInput, bArr, readFromExtractorInput, min2 - readFromExtractorInput);
        }
        int i = readFromExtractorInput;
        byteBuffer.put(bArr, 0, i);
        return i;
    }

    public void release() {
        flacRelease(this.nativeDecoderContext);
    }

    public void reset(long j) {
        flacReset(this.nativeDecoderContext, j);
    }

    public void setData(ExtractorInput extractorInput) {
        this.byteBufferData = null;
        this.extractorInput = extractorInput;
        this.endOfExtractorInput = false;
        if (this.tempBuffer == null) {
            this.tempBuffer = new byte[8192];
        }
    }

    public void setData(ByteBuffer byteBuffer) {
        this.byteBufferData = byteBuffer;
        this.extractorInput = null;
    }
}
