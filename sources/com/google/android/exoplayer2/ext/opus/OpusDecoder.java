package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.decoder.CryptoException;
import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.DecoderOutputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleDecoderOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
/* loaded from: classes.dex */
public final class OpusDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleDecoderOutputBuffer, OpusDecoderException> {
    private static final int DECODE_ERROR = -1;
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int DRM_ERROR = -2;
    private static final int FULL_CODEC_INITIALIZATION_DATA_BUFFER_COUNT = 3;
    private static final int NO_ERROR = 0;
    static final int SAMPLE_RATE = 48000;
    public final int channelCount;
    private final CryptoConfig cryptoConfig;
    private boolean experimentalDiscardPaddingEnabled;
    private final long nativeDecoderContext;
    public final boolean outputFloat;
    private final int preSkipSamples;
    private final int seekPreRollSamples;
    private int skipSamples;

    private native void opusClose(long j);

    private native int opusDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleDecoderOutputBuffer simpleDecoderOutputBuffer);

    private native int opusGetErrorCode(long j);

    private native String opusGetErrorMessage(long j);

    private native long opusInit(int i, int i2, int i3, int i4, int i5, byte[] bArr);

    private native void opusReset(long j);

    private native int opusSecureDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleDecoderOutputBuffer simpleDecoderOutputBuffer, int i2, CryptoConfig cryptoConfig, int i3, byte[] bArr, byte[] bArr2, int i4, int[] iArr, int[] iArr2);

    private native void opusSetFloatOutput();

    private static int samplesToBytes(int i, int i2, boolean z) {
        return i * i2 * (z ? 4 : 2);
    }

    public OpusDecoder(int i, int i2, int i3, List<byte[]> list, CryptoConfig cryptoConfig, boolean z) throws OpusDecoderException {
        super(new DecoderInputBuffer[i], new SimpleDecoderOutputBuffer[i2]);
        int i4;
        if (!OpusLibrary.isAvailable()) {
            throw new OpusDecoderException("Failed to load decoder native libraries");
        }
        this.cryptoConfig = cryptoConfig;
        if (cryptoConfig != null && !OpusLibrary.opusIsSecureDecodeSupported()) {
            throw new OpusDecoderException("Opus decoder does not support secure decode");
        }
        int size = list.size();
        int i5 = 1;
        if (size != 1 && size != 3) {
            throw new OpusDecoderException("Invalid initialization data size");
        }
        if (size == 3 && (list.get(1).length != 8 || list.get(2).length != 8)) {
            throw new OpusDecoderException("Invalid pre-skip or seek pre-roll");
        }
        int preSkipSamples = getPreSkipSamples(list);
        this.preSkipSamples = preSkipSamples;
        this.seekPreRollSamples = getSeekPreRollSamples(list);
        this.skipSamples = preSkipSamples;
        byte[] bArr = list.get(0);
        if (bArr.length < 19) {
            throw new OpusDecoderException("Invalid header length");
        }
        int channelCount = getChannelCount(bArr);
        this.channelCount = channelCount;
        if (channelCount > 8) {
            throw new OpusDecoderException("Invalid channel count: " + channelCount);
        }
        int readSignedLittleEndian16 = readSignedLittleEndian16(bArr, 16);
        byte[] bArr2 = new byte[8];
        if (bArr[18] == 0) {
            if (channelCount > 2) {
                throw new OpusDecoderException("Invalid header, missing stream map");
            }
            int i6 = channelCount == 2 ? 1 : 0;
            bArr2[0] = 0;
            bArr2[1] = 1;
            i4 = i6;
        } else if (bArr.length < channelCount + 21) {
            throw new OpusDecoderException("Invalid header length");
        } else {
            i5 = bArr[19] & 255;
            i4 = bArr[20] & 255;
            System.arraycopy(bArr, 21, bArr2, 0, channelCount);
        }
        long opusInit = opusInit(SAMPLE_RATE, channelCount, i5, i4, readSignedLittleEndian16, bArr2);
        this.nativeDecoderContext = opusInit;
        if (opusInit == 0) {
            throw new OpusDecoderException("Failed to initialize decoder");
        }
        setInitialInputBufferSize(i3);
        this.outputFloat = z;
        if (z) {
            opusSetFloatOutput();
        }
    }

    public void experimentalSetDiscardPaddingEnabled(boolean z) {
        this.experimentalDiscardPaddingEnabled = z;
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public String getName() {
        return "libopus" + OpusLibrary.getVersion();
    }

    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    protected DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public SimpleDecoderOutputBuffer createOutputBuffer() {
        return new SimpleDecoderOutputBuffer(new DecoderOutputBuffer.Owner() { // from class: com.google.android.exoplayer2.ext.opus.OpusDecoder$$ExternalSyntheticLambda0
            @Override // com.google.android.exoplayer2.decoder.DecoderOutputBuffer.Owner
            public final void releaseOutputBuffer(DecoderOutputBuffer decoderOutputBuffer) {
                OpusDecoder.this.releaseOutputBuffer((SimpleDecoderOutputBuffer) decoderOutputBuffer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public OpusDecoderException createUnexpectedDecodeException(Throwable th) {
        return new OpusDecoderException("Unexpected decode error", th);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public OpusDecoderException decode(DecoderInputBuffer decoderInputBuffer, SimpleDecoderOutputBuffer simpleDecoderOutputBuffer, boolean z) {
        OpusDecoder opusDecoder;
        DecoderInputBuffer decoderInputBuffer2;
        int opusDecode;
        int discardPaddingSamples;
        int samplesToBytes;
        if (z) {
            opusReset(this.nativeDecoderContext);
            this.skipSamples = decoderInputBuffer.timeUs == 0 ? this.preSkipSamples : this.seekPreRollSamples;
        }
        ByteBuffer byteBuffer = (ByteBuffer) Util.castNonNull(decoderInputBuffer.data);
        CryptoInfo cryptoInfo = decoderInputBuffer.cryptoInfo;
        if (decoderInputBuffer.isEncrypted()) {
            opusDecode = opusSecureDecode(this.nativeDecoderContext, decoderInputBuffer.timeUs, byteBuffer, byteBuffer.limit(), simpleDecoderOutputBuffer, SAMPLE_RATE, this.cryptoConfig, cryptoInfo.mode, (byte[]) Assertions.checkNotNull(cryptoInfo.key), (byte[]) Assertions.checkNotNull(cryptoInfo.iv), cryptoInfo.numSubSamples, cryptoInfo.numBytesOfClearData, cryptoInfo.numBytesOfEncryptedData);
            opusDecoder = this;
            decoderInputBuffer2 = decoderInputBuffer;
        } else {
            opusDecoder = this;
            decoderInputBuffer2 = decoderInputBuffer;
            opusDecode = opusDecode(opusDecoder.nativeDecoderContext, decoderInputBuffer2.timeUs, byteBuffer, byteBuffer.limit(), simpleDecoderOutputBuffer);
        }
        if (opusDecode < 0) {
            if (opusDecode == -2) {
                String str = "Drm error: " + opusDecoder.opusGetErrorMessage(opusDecoder.nativeDecoderContext);
                return new OpusDecoderException(str, new CryptoException(opusDecoder.opusGetErrorCode(opusDecoder.nativeDecoderContext), str));
            }
            return new OpusDecoderException("Decode error: " + opusDecoder.opusGetErrorMessage(opusDecode));
        }
        ByteBuffer byteBuffer2 = (ByteBuffer) Util.castNonNull(simpleDecoderOutputBuffer.data);
        byteBuffer2.position(0);
        byteBuffer2.limit(opusDecode);
        if (opusDecoder.skipSamples > 0) {
            int samplesToBytes2 = samplesToBytes(1, opusDecoder.channelCount, opusDecoder.outputFloat);
            int i = opusDecoder.skipSamples;
            int i2 = i * samplesToBytes2;
            if (opusDecode <= i2) {
                opusDecoder.skipSamples = i - (opusDecode / samplesToBytes2);
                simpleDecoderOutputBuffer.addFlag(Integer.MIN_VALUE);
                byteBuffer2.position(opusDecode);
                return null;
            }
            opusDecoder.skipSamples = 0;
            byteBuffer2.position(i2);
            return null;
        } else if (!opusDecoder.experimentalDiscardPaddingEnabled || !decoderInputBuffer.hasSupplementalData() || (discardPaddingSamples = getDiscardPaddingSamples(decoderInputBuffer2.supplementalData)) <= 0 || opusDecode < (samplesToBytes = samplesToBytes(discardPaddingSamples, opusDecoder.channelCount, opusDecoder.outputFloat))) {
            return null;
        } else {
            byteBuffer2.limit(opusDecode - samplesToBytes);
            return null;
        }
    }

    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void release() {
        super.release();
        opusClose(this.nativeDecoderContext);
    }

    static int getChannelCount(byte[] bArr) {
        return bArr[9] & 255;
    }

    static int getPreSkipSamples(List<byte[]> list) {
        if (list.size() == 3) {
            return (int) ((ByteBuffer.wrap(list.get(1)).order(ByteOrder.nativeOrder()).getLong() * 48000) / 1000000000);
        }
        byte[] bArr = list.get(0);
        return (bArr[10] & 255) | ((bArr[11] & 255) << 8);
    }

    static int getSeekPreRollSamples(List<byte[]> list) {
        return list.size() == 3 ? (int) ((ByteBuffer.wrap(list.get(2)).order(ByteOrder.nativeOrder()).getLong() * 48000) / 1000000000) : DEFAULT_SEEK_PRE_ROLL_SAMPLES;
    }

    static int getDiscardPaddingSamples(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.remaining() != 8) {
            return 0;
        }
        long j = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getLong();
        if (j < 0) {
            return 0;
        }
        return (int) ((j * 48000) / 1000000000);
    }

    private static int readSignedLittleEndian16(byte[] bArr, int i) {
        return (short) (((bArr[i + 1] & 255) << 8) | (bArr[i] & 255));
    }
}
