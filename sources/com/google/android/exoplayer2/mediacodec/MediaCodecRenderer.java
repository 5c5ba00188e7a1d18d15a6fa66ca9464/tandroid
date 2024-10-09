package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaFormat;
import android.media.metrics.LogSessionId;
import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.FrameworkCryptoConfig;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.TimedValueQueue;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.telegram.messenger.MediaController;

/* loaded from: classes.dex */
public abstract class MediaCodecRenderer extends BaseRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = {0, 0, 1, 103, 66, -64, 11, -38, 37, -112, 0, 0, 1, 104, -50, 15, 19, 32, 0, 0, 1, 101, -120, -124, 13, -50, 113, 24, -96, 0, 47, -65, 28, 49, -61, 39, 93, 120};
    private final float assumedMinimumCodecOperatingRate;
    private ArrayDeque availableCodecInfos;
    private final DecoderInputBuffer buffer;
    private final BatchBuffer bypassBatchBuffer;
    private boolean bypassDrainAndReinitialize;
    private boolean bypassEnabled;
    private final DecoderInputBuffer bypassSampleBuffer;
    private boolean bypassSampleBufferPending;
    private C2Mp3TimestampTracker c2Mp3TimestampTracker;
    private MediaCodecAdapter codec;
    private int codecAdaptationWorkaroundMode;
    private final MediaCodecAdapter.Factory codecAdapterFactory;
    private int codecDrainAction;
    private int codecDrainState;
    private DrmSession codecDrmSession;
    private boolean codecHasOutputMediaFormat;
    private long codecHotswapDeadlineMs;
    private MediaCodecInfo codecInfo;
    private Format codecInputFormat;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsDiscardToSpsWorkaround;
    private boolean codecNeedsEosBufferTimestampWorkaround;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosOutputExceptionWorkaround;
    private boolean codecNeedsEosPropagation;
    private boolean codecNeedsFlushWorkaround;
    private boolean codecNeedsMonoChannelCountWorkaround;
    private boolean codecNeedsSosFlushWorkaround;
    private float codecOperatingRate;
    public MediaFormat codecOutputMediaFormat;
    private boolean codecOutputMediaFormatChanged;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private float currentPlaybackSpeed;
    private final ArrayList decodeOnlyPresentationTimestamps;
    protected DecoderCounters decoderCounters;
    private final boolean enableDecoderFallback;
    private final TimedValueQueue formatQueue;
    private Format inputFormat;
    private int inputIndex;
    private boolean inputStreamEnded;
    private boolean isDecodeOnlyOutputBuffer;
    private boolean isLastOutputBuffer;
    private long largestQueuedPresentationTimeUs;
    private long lastBufferInStreamPresentationTimeUs;
    private final MediaCodecSelector mediaCodecSelector;
    private MediaCrypto mediaCrypto;
    private boolean mediaCryptoRequiresSecureDecoder;
    private final DecoderInputBuffer noDataBuffer;
    private ByteBuffer outputBuffer;
    private final MediaCodec.BufferInfo outputBufferInfo;
    private Format outputFormat;
    private int outputIndex;
    private boolean outputStreamEnded;
    private long outputStreamOffsetUs;
    private long outputStreamStartPositionUs;
    private boolean pendingOutputEndOfStream;
    private int pendingOutputStreamOffsetCount;
    private final long[] pendingOutputStreamOffsetsUs;
    private final long[] pendingOutputStreamStartPositionsUs;
    private final long[] pendingOutputStreamSwitchTimesUs;
    private ExoPlaybackException pendingPlaybackException;
    private DecoderInitializationException preferredDecoderInitializationException;
    private long renderTimeLimitMs;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private DrmSession sourceDrmSession;
    private float targetPlaybackSpeed;
    private boolean waitingForFirstSampleInFormat;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Api31 {
        public static void setLogSessionIdToMediaCodecFormat(MediaCodecAdapter.Configuration configuration, PlayerId playerId) {
            LogSessionId logSessionId;
            boolean equals;
            String stringId;
            LogSessionId logSessionId2 = playerId.getLogSessionId();
            logSessionId = LogSessionId.LOG_SESSION_ID_NONE;
            equals = logSessionId2.equals(logSessionId);
            if (equals) {
                return;
            }
            MediaFormat mediaFormat = configuration.mediaFormat;
            stringId = logSessionId2.getStringId();
            mediaFormat.setString("log-session-id", stringId);
        }
    }

    /* loaded from: classes.dex */
    public static class DecoderInitializationException extends Exception {
        public final MediaCodecInfo codecInfo;
        public final String diagnosticInfo;
        public final DecoderInitializationException fallbackDecoderInitializationException;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(Format format, Throwable th, boolean z, int i) {
            this("Decoder init failed: [" + i + "], " + format, th, format.sampleMimeType, z, null, buildCustomDiagnosticInfo(i), null);
        }

        public DecoderInitializationException(Format format, Throwable th, boolean z, MediaCodecInfo mediaCodecInfo) {
            this("Decoder init failed: " + mediaCodecInfo.name + ", " + format, th, format.sampleMimeType, z, mediaCodecInfo, Util.SDK_INT >= 21 ? getDiagnosticInfoV21(th) : null, null);
        }

        private DecoderInitializationException(String str, Throwable th, String str2, boolean z, MediaCodecInfo mediaCodecInfo, String str3, DecoderInitializationException decoderInitializationException) {
            super(str, th);
            this.mimeType = str2;
            this.secureDecoderRequired = z;
            this.codecInfo = mediaCodecInfo;
            this.diagnosticInfo = str3;
            this.fallbackDecoderInitializationException = decoderInitializationException;
        }

        private static String buildCustomDiagnosticInfo(int i) {
            return "com.google.android.exoplayer2.mediacodec.MediaCodecRenderer_" + (i < 0 ? "neg_" : "") + Math.abs(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public DecoderInitializationException copyWithFallbackException(DecoderInitializationException decoderInitializationException) {
            return new DecoderInitializationException(getMessage(), getCause(), this.mimeType, this.secureDecoderRequired, this.codecInfo, this.diagnosticInfo, decoderInitializationException);
        }

        private static String getDiagnosticInfoV21(Throwable th) {
            String diagnosticInfo;
            if (!MediaCodecDecoderException$$ExternalSyntheticApiModelOutline0.m(th)) {
                return null;
            }
            diagnosticInfo = MediaCodecDecoderException$$ExternalSyntheticApiModelOutline1.m(th).getDiagnosticInfo();
            return diagnosticInfo;
        }
    }

    public MediaCodecRenderer(int i, MediaCodecAdapter.Factory factory, MediaCodecSelector mediaCodecSelector, boolean z, float f) {
        super(i);
        this.codecAdapterFactory = factory;
        this.mediaCodecSelector = (MediaCodecSelector) Assertions.checkNotNull(mediaCodecSelector);
        this.enableDecoderFallback = z;
        this.assumedMinimumCodecOperatingRate = f;
        this.noDataBuffer = DecoderInputBuffer.newNoDataInstance();
        this.buffer = new DecoderInputBuffer(0);
        this.bypassSampleBuffer = new DecoderInputBuffer(2);
        BatchBuffer batchBuffer = new BatchBuffer();
        this.bypassBatchBuffer = batchBuffer;
        this.formatQueue = new TimedValueQueue();
        this.decodeOnlyPresentationTimestamps = new ArrayList();
        this.outputBufferInfo = new MediaCodec.BufferInfo();
        this.currentPlaybackSpeed = 1.0f;
        this.targetPlaybackSpeed = 1.0f;
        this.renderTimeLimitMs = -9223372036854775807L;
        this.pendingOutputStreamStartPositionsUs = new long[10];
        this.pendingOutputStreamOffsetsUs = new long[10];
        this.pendingOutputStreamSwitchTimesUs = new long[10];
        this.outputStreamStartPositionUs = -9223372036854775807L;
        setOutputStreamOffsetUs(-9223372036854775807L);
        batchBuffer.ensureSpaceForWrite(0);
        batchBuffer.data.order(ByteOrder.nativeOrder());
        this.codecOperatingRate = -1.0f;
        this.codecAdaptationWorkaroundMode = 0;
        this.codecReconfigurationState = 0;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.codecHotswapDeadlineMs = -9223372036854775807L;
        this.largestQueuedPresentationTimeUs = -9223372036854775807L;
        this.lastBufferInStreamPresentationTimeUs = -9223372036854775807L;
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
    }

    private void bypassRead() {
        Assertions.checkState(!this.inputStreamEnded);
        FormatHolder formatHolder = getFormatHolder();
        this.bypassSampleBuffer.clear();
        do {
            this.bypassSampleBuffer.clear();
            int readSource = readSource(formatHolder, this.bypassSampleBuffer, 0);
            if (readSource == -5) {
                onInputFormatChanged(formatHolder);
                return;
            }
            if (readSource != -4) {
                if (readSource != -3) {
                    throw new IllegalStateException();
                }
                return;
            } else {
                if (this.bypassSampleBuffer.isEndOfStream()) {
                    this.inputStreamEnded = true;
                    return;
                }
                if (this.waitingForFirstSampleInFormat) {
                    Format format = (Format) Assertions.checkNotNull(this.inputFormat);
                    this.outputFormat = format;
                    onOutputFormatChanged(format, null);
                    this.waitingForFirstSampleInFormat = false;
                }
                this.bypassSampleBuffer.flip();
            }
        } while (this.bypassBatchBuffer.append(this.bypassSampleBuffer));
        this.bypassSampleBufferPending = true;
    }

    private boolean bypassRender(long j, long j2) {
        Assertions.checkState(!this.outputStreamEnded);
        if (this.bypassBatchBuffer.hasSamples()) {
            BatchBuffer batchBuffer = this.bypassBatchBuffer;
            if (!processOutputBuffer(j, j2, null, batchBuffer.data, this.outputIndex, 0, batchBuffer.getSampleCount(), this.bypassBatchBuffer.getFirstSampleTimeUs(), this.bypassBatchBuffer.isDecodeOnly(), this.bypassBatchBuffer.isEndOfStream(), this.outputFormat)) {
                return false;
            }
            onProcessedOutputBuffer(this.bypassBatchBuffer.getLastSampleTimeUs());
            this.bypassBatchBuffer.clear();
        }
        if (this.inputStreamEnded) {
            this.outputStreamEnded = true;
            return false;
        }
        if (this.bypassSampleBufferPending) {
            Assertions.checkState(this.bypassBatchBuffer.append(this.bypassSampleBuffer));
            this.bypassSampleBufferPending = false;
        }
        if (this.bypassDrainAndReinitialize) {
            if (this.bypassBatchBuffer.hasSamples()) {
                return true;
            }
            disableBypass();
            this.bypassDrainAndReinitialize = false;
            maybeInitCodecOrBypass();
            if (!this.bypassEnabled) {
                return false;
            }
        }
        bypassRead();
        if (this.bypassBatchBuffer.hasSamples()) {
            this.bypassBatchBuffer.flip();
        }
        return this.bypassBatchBuffer.hasSamples() || this.inputStreamEnded || this.bypassDrainAndReinitialize;
    }

    private int codecAdaptationWorkaroundMode(String str) {
        int i = Util.SDK_INT;
        if (i <= 25 && "OMX.Exynos.avc.dec.secure".equals(str)) {
            String str2 = Util.MODEL;
            if (str2.startsWith("SM-T585") || str2.startsWith("SM-A510") || str2.startsWith("SM-A520") || str2.startsWith("SM-J700")) {
                return 2;
            }
        }
        if (i >= 24) {
            return 0;
        }
        if (!"OMX.Nvidia.h264.decode".equals(str) && !"OMX.Nvidia.h264.decode.secure".equals(str)) {
            return 0;
        }
        String str3 = Util.DEVICE;
        return ("flounder".equals(str3) || "flounder_lte".equals(str3) || "grouper".equals(str3) || "tilapia".equals(str3)) ? 1 : 0;
    }

    private static boolean codecNeedsDiscardToSpsWorkaround(String str, Format format) {
        return Util.SDK_INT < 21 && format.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(str);
    }

    private static boolean codecNeedsEosBufferTimestampWorkaround(String str) {
        if (Util.SDK_INT < 21 && "OMX.SEC.mp3.dec".equals(str) && "samsung".equals(Util.MANUFACTURER)) {
            String str2 = Util.DEVICE;
            if (str2.startsWith("baffin") || str2.startsWith("grand") || str2.startsWith("fortuna") || str2.startsWith("gprimelte") || str2.startsWith("j2y18lte") || str2.startsWith("ms01")) {
                return true;
            }
        }
        return false;
    }

    private static boolean codecNeedsEosFlushWorkaround(String str) {
        int i = Util.SDK_INT;
        if (i > 23 || !"OMX.google.vorbis.decoder".equals(str)) {
            if (i <= 19) {
                String str2 = Util.DEVICE;
                if (("hb2000".equals(str2) || "stvm8".equals(str2)) && ("OMX.amlogic.avc.decoder.awesome".equals(str) || "OMX.amlogic.avc.decoder.awesome.secure".equals(str))) {
                }
            }
            return false;
        }
        return true;
    }

    private static boolean codecNeedsEosOutputExceptionWorkaround(String str) {
        return Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(str);
    }

    private static boolean codecNeedsEosPropagationWorkaround(MediaCodecInfo mediaCodecInfo) {
        String str = mediaCodecInfo.name;
        int i = Util.SDK_INT;
        return (i <= 25 && "OMX.rk.video_decoder.avc".equals(str)) || (i <= 17 && "OMX.allwinner.video.decoder.avc".equals(str)) || ((i <= 29 && ("OMX.broadcom.video_decoder.tunnel".equals(str) || "OMX.broadcom.video_decoder.tunnel.secure".equals(str) || "OMX.bcm.vdec.avc.tunnel".equals(str) || "OMX.bcm.vdec.avc.tunnel.secure".equals(str) || "OMX.bcm.vdec.hevc.tunnel".equals(str) || "OMX.bcm.vdec.hevc.tunnel.secure".equals(str))) || ("Amazon".equals(Util.MANUFACTURER) && "AFTS".equals(Util.MODEL) && mediaCodecInfo.secure));
    }

    private static boolean codecNeedsFlushWorkaround(String str) {
        int i = Util.SDK_INT;
        return i < 18 || (i == 18 && ("OMX.SEC.avc.dec".equals(str) || "OMX.SEC.avc.dec.secure".equals(str))) || (i == 19 && Util.MODEL.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(str) || "OMX.Exynos.avc.dec.secure".equals(str)));
    }

    private static boolean codecNeedsMonoChannelCountWorkaround(String str, Format format) {
        return Util.SDK_INT <= 18 && format.channelCount == 1 && "OMX.MTK.AUDIO.DECODER.MP3".equals(str);
    }

    private static boolean codecNeedsSosFlushWorkaround(String str) {
        return Util.SDK_INT == 29 && "c2.android.aac.decoder".equals(str);
    }

    private void disableBypass() {
        this.bypassDrainAndReinitialize = false;
        this.bypassBatchBuffer.clear();
        this.bypassSampleBuffer.clear();
        this.bypassSampleBufferPending = false;
        this.bypassEnabled = false;
    }

    private boolean drainAndFlushCodec() {
        if (this.codecReceivedBuffers) {
            this.codecDrainState = 1;
            if (this.codecNeedsFlushWorkaround || this.codecNeedsEosFlushWorkaround) {
                this.codecDrainAction = 3;
                return false;
            }
            this.codecDrainAction = 1;
        }
        return true;
    }

    private void drainAndReinitializeCodec() {
        if (!this.codecReceivedBuffers) {
            reinitializeCodec();
        } else {
            this.codecDrainState = 1;
            this.codecDrainAction = 3;
        }
    }

    private boolean drainAndUpdateCodecDrmSessionV23() {
        if (this.codecReceivedBuffers) {
            this.codecDrainState = 1;
            if (this.codecNeedsFlushWorkaround || this.codecNeedsEosFlushWorkaround) {
                this.codecDrainAction = 3;
                return false;
            }
            this.codecDrainAction = 2;
        } else {
            updateDrmSessionV23();
        }
        return true;
    }

    private boolean drainOutputBuffer(long j, long j2) {
        boolean z;
        boolean processOutputBuffer;
        int dequeueOutputBufferIndex;
        if (!hasOutputBuffer()) {
            if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
                try {
                    dequeueOutputBufferIndex = this.codec.dequeueOutputBufferIndex(this.outputBufferInfo);
                } catch (IllegalStateException unused) {
                    processEndOfStream();
                    if (this.outputStreamEnded) {
                        releaseCodec();
                    }
                    return false;
                }
            } else {
                dequeueOutputBufferIndex = this.codec.dequeueOutputBufferIndex(this.outputBufferInfo);
            }
            if (dequeueOutputBufferIndex < 0) {
                if (dequeueOutputBufferIndex == -2) {
                    processOutputMediaFormatChanged();
                    return true;
                }
                if (this.codecNeedsEosPropagation && (this.inputStreamEnded || this.codecDrainState == 2)) {
                    processEndOfStream();
                }
                return false;
            }
            if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
                this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
                this.codec.releaseOutputBuffer(dequeueOutputBufferIndex, false);
                return true;
            }
            MediaCodec.BufferInfo bufferInfo = this.outputBufferInfo;
            if (bufferInfo.size == 0 && (bufferInfo.flags & 4) != 0) {
                processEndOfStream();
                return false;
            }
            this.outputIndex = dequeueOutputBufferIndex;
            ByteBuffer outputBuffer = this.codec.getOutputBuffer(dequeueOutputBufferIndex);
            this.outputBuffer = outputBuffer;
            if (outputBuffer != null) {
                outputBuffer.position(this.outputBufferInfo.offset);
                ByteBuffer byteBuffer = this.outputBuffer;
                MediaCodec.BufferInfo bufferInfo2 = this.outputBufferInfo;
                byteBuffer.limit(bufferInfo2.offset + bufferInfo2.size);
            }
            if (this.codecNeedsEosBufferTimestampWorkaround) {
                MediaCodec.BufferInfo bufferInfo3 = this.outputBufferInfo;
                if (bufferInfo3.presentationTimeUs == 0 && (bufferInfo3.flags & 4) != 0) {
                    long j3 = this.largestQueuedPresentationTimeUs;
                    if (j3 != -9223372036854775807L) {
                        bufferInfo3.presentationTimeUs = j3;
                    }
                }
            }
            this.isDecodeOnlyOutputBuffer = isDecodeOnlyBuffer(this.outputBufferInfo.presentationTimeUs);
            long j4 = this.lastBufferInStreamPresentationTimeUs;
            long j5 = this.outputBufferInfo.presentationTimeUs;
            this.isLastOutputBuffer = j4 == j5;
            updateOutputFormatForTime(j5);
        }
        if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
            try {
                MediaCodecAdapter mediaCodecAdapter = this.codec;
                ByteBuffer byteBuffer2 = this.outputBuffer;
                int i = this.outputIndex;
                MediaCodec.BufferInfo bufferInfo4 = this.outputBufferInfo;
                z = false;
                try {
                    processOutputBuffer = processOutputBuffer(j, j2, mediaCodecAdapter, byteBuffer2, i, bufferInfo4.flags, 1, bufferInfo4.presentationTimeUs, this.isDecodeOnlyOutputBuffer, this.isLastOutputBuffer, this.outputFormat);
                } catch (IllegalStateException unused2) {
                    processEndOfStream();
                    if (this.outputStreamEnded) {
                        releaseCodec();
                    }
                    return z;
                }
            } catch (IllegalStateException unused3) {
                z = false;
            }
        } else {
            z = false;
            MediaCodecAdapter mediaCodecAdapter2 = this.codec;
            ByteBuffer byteBuffer3 = this.outputBuffer;
            int i2 = this.outputIndex;
            MediaCodec.BufferInfo bufferInfo5 = this.outputBufferInfo;
            processOutputBuffer = processOutputBuffer(j, j2, mediaCodecAdapter2, byteBuffer3, i2, bufferInfo5.flags, 1, bufferInfo5.presentationTimeUs, this.isDecodeOnlyOutputBuffer, this.isLastOutputBuffer, this.outputFormat);
        }
        if (processOutputBuffer) {
            onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            boolean z2 = (this.outputBufferInfo.flags & 4) != 0;
            resetOutputBuffer();
            if (!z2) {
                return true;
            }
            processEndOfStream();
        }
        return z;
    }

    private boolean drmNeedsCodecReinitialization(MediaCodecInfo mediaCodecInfo, Format format, DrmSession drmSession, DrmSession drmSession2) {
        FrameworkCryptoConfig frameworkCryptoConfig;
        if (drmSession == drmSession2) {
            return false;
        }
        if (drmSession2 == null || drmSession == null || !drmSession2.getSchemeUuid().equals(drmSession.getSchemeUuid()) || Util.SDK_INT < 23) {
            return true;
        }
        UUID uuid = C.PLAYREADY_UUID;
        if (uuid.equals(drmSession.getSchemeUuid()) || uuid.equals(drmSession2.getSchemeUuid()) || (frameworkCryptoConfig = getFrameworkCryptoConfig(drmSession2)) == null) {
            return true;
        }
        return !mediaCodecInfo.secure && (frameworkCryptoConfig.forceAllowInsecureDecoderComponents ? false : drmSession2.requiresSecureDecoder(format.sampleMimeType));
    }

    private boolean feedInputBuffer() {
        int i;
        if (this.codec == null || (i = this.codecDrainState) == 2 || this.inputStreamEnded) {
            return false;
        }
        if (i == 0 && shouldReinitCodec()) {
            drainAndReinitializeCodec();
        }
        if (this.inputIndex < 0) {
            int dequeueInputBufferIndex = this.codec.dequeueInputBufferIndex();
            this.inputIndex = dequeueInputBufferIndex;
            if (dequeueInputBufferIndex < 0) {
                return false;
            }
            this.buffer.data = this.codec.getInputBuffer(dequeueInputBufferIndex);
            this.buffer.clear();
        }
        if (this.codecDrainState == 1) {
            if (!this.codecNeedsEosPropagation) {
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                resetInputBuffer();
            }
            this.codecDrainState = 2;
            return false;
        }
        if (this.codecNeedsAdaptationWorkaroundBuffer) {
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            ByteBuffer byteBuffer = this.buffer.data;
            byte[] bArr = ADAPTATION_WORKAROUND_BUFFER;
            byteBuffer.put(bArr);
            this.codec.queueInputBuffer(this.inputIndex, 0, bArr.length, 0L, 0);
            resetInputBuffer();
            this.codecReceivedBuffers = true;
            return true;
        }
        if (this.codecReconfigurationState == 1) {
            for (int i2 = 0; i2 < this.codecInputFormat.initializationData.size(); i2++) {
                this.buffer.data.put((byte[]) this.codecInputFormat.initializationData.get(i2));
            }
            this.codecReconfigurationState = 2;
        }
        int position = this.buffer.data.position();
        FormatHolder formatHolder = getFormatHolder();
        try {
            int readSource = readSource(formatHolder, this.buffer, 0);
            if (hasReadStreamToEnd()) {
                this.lastBufferInStreamPresentationTimeUs = this.largestQueuedPresentationTimeUs;
            }
            if (readSource == -3) {
                return false;
            }
            if (readSource == -5) {
                if (this.codecReconfigurationState == 2) {
                    this.buffer.clear();
                    this.codecReconfigurationState = 1;
                }
                onInputFormatChanged(formatHolder);
                return true;
            }
            if (this.buffer.isEndOfStream()) {
                if (this.codecReconfigurationState == 2) {
                    this.buffer.clear();
                    this.codecReconfigurationState = 1;
                }
                this.inputStreamEnded = true;
                if (!this.codecReceivedBuffers) {
                    processEndOfStream();
                    return false;
                }
                try {
                    if (!this.codecNeedsEosPropagation) {
                        this.codecReceivedEos = true;
                        this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                        resetInputBuffer();
                    }
                    return false;
                } catch (MediaCodec.CryptoException e) {
                    throw createRendererException(e, this.inputFormat, Util.getErrorCodeForMediaDrmErrorCode(e.getErrorCode()));
                }
            }
            if (!this.codecReceivedBuffers && !this.buffer.isKeyFrame()) {
                this.buffer.clear();
                if (this.codecReconfigurationState == 2) {
                    this.codecReconfigurationState = 1;
                }
                return true;
            }
            boolean isEncrypted = this.buffer.isEncrypted();
            if (isEncrypted) {
                this.buffer.cryptoInfo.increaseClearDataFirstSubSampleBy(position);
            }
            if (this.codecNeedsDiscardToSpsWorkaround && !isEncrypted) {
                NalUnitUtil.discardToSps(this.buffer.data);
                if (this.buffer.data.position() == 0) {
                    return true;
                }
                this.codecNeedsDiscardToSpsWorkaround = false;
            }
            DecoderInputBuffer decoderInputBuffer = this.buffer;
            long j = decoderInputBuffer.timeUs;
            C2Mp3TimestampTracker c2Mp3TimestampTracker = this.c2Mp3TimestampTracker;
            if (c2Mp3TimestampTracker != null) {
                j = c2Mp3TimestampTracker.updateAndGetPresentationTimeUs(this.inputFormat, decoderInputBuffer);
                this.largestQueuedPresentationTimeUs = Math.max(this.largestQueuedPresentationTimeUs, this.c2Mp3TimestampTracker.getLastOutputBufferPresentationTimeUs(this.inputFormat));
            }
            long j2 = j;
            if (this.buffer.isDecodeOnly()) {
                this.decodeOnlyPresentationTimestamps.add(Long.valueOf(j2));
            }
            if (this.waitingForFirstSampleInFormat) {
                this.formatQueue.add(j2, this.inputFormat);
                this.waitingForFirstSampleInFormat = false;
            }
            this.largestQueuedPresentationTimeUs = Math.max(this.largestQueuedPresentationTimeUs, j2);
            this.buffer.flip();
            if (this.buffer.hasSupplementalData()) {
                handleInputBufferSupplementalData(this.buffer);
            }
            onQueueInputBuffer(this.buffer);
            try {
                if (isEncrypted) {
                    this.codec.queueSecureInputBuffer(this.inputIndex, 0, this.buffer.cryptoInfo, j2, 0);
                } else {
                    this.codec.queueInputBuffer(this.inputIndex, 0, this.buffer.data.limit(), j2, 0);
                }
                resetInputBuffer();
                this.codecReceivedBuffers = true;
                this.codecReconfigurationState = 0;
                this.decoderCounters.queuedInputBufferCount++;
                return true;
            } catch (MediaCodec.CryptoException e2) {
                throw createRendererException(e2, this.inputFormat, Util.getErrorCodeForMediaDrmErrorCode(e2.getErrorCode()));
            }
        } catch (DecoderInputBuffer.InsufficientCapacityException e3) {
            onCodecError(e3);
            readSourceOmittingSampleData(0);
            flushCodec();
            return true;
        }
    }

    private void flushCodec() {
        try {
            this.codec.flush();
        } finally {
            resetCodecStateForFlush();
        }
    }

    private List getAvailableCodecInfos(boolean z) {
        List decoderInfos = getDecoderInfos(this.mediaCodecSelector, this.inputFormat, z);
        if (decoderInfos.isEmpty() && z) {
            decoderInfos = getDecoderInfos(this.mediaCodecSelector, this.inputFormat, false);
            if (!decoderInfos.isEmpty()) {
                Log.w("MediaCodecRenderer", "Drm session requires secure decoder for " + this.inputFormat.sampleMimeType + ", but no secure decoder available. Trying to proceed with " + decoderInfos + ".");
            }
        }
        return decoderInfos;
    }

    private FrameworkCryptoConfig getFrameworkCryptoConfig(DrmSession drmSession) {
        CryptoConfig cryptoConfig = drmSession.getCryptoConfig();
        if (cryptoConfig == null || (cryptoConfig instanceof FrameworkCryptoConfig)) {
            return (FrameworkCryptoConfig) cryptoConfig;
        }
        throw createRendererException(new IllegalArgumentException("Expecting FrameworkCryptoConfig but found: " + cryptoConfig), this.inputFormat, 6001);
    }

    private boolean hasOutputBuffer() {
        return this.outputIndex >= 0;
    }

    private void initBypass(Format format) {
        disableBypass();
        String str = format.sampleMimeType;
        if (MediaController.AUDIO_MIME_TYPE.equals(str) || "audio/mpeg".equals(str) || "audio/opus".equals(str)) {
            this.bypassBatchBuffer.setMaxSampleCount(32);
        } else {
            this.bypassBatchBuffer.setMaxSampleCount(1);
        }
        this.bypassEnabled = true;
    }

    private void initCodec(MediaCodecInfo mediaCodecInfo, MediaCrypto mediaCrypto) {
        String str = mediaCodecInfo.name;
        int i = Util.SDK_INT;
        float codecOperatingRateV23 = i < 23 ? -1.0f : getCodecOperatingRateV23(this.targetPlaybackSpeed, this.inputFormat, getStreamFormats());
        float f = codecOperatingRateV23 > this.assumedMinimumCodecOperatingRate ? codecOperatingRateV23 : -1.0f;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        MediaCodecAdapter.Configuration mediaCodecConfiguration = getMediaCodecConfiguration(mediaCodecInfo, this.inputFormat, mediaCrypto, f);
        if (i >= 31) {
            Api31.setLogSessionIdToMediaCodecFormat(mediaCodecConfiguration, getPlayerId());
        }
        try {
            TraceUtil.beginSection("createCodec:" + str);
            this.codec = this.codecAdapterFactory.createAdapter(mediaCodecConfiguration);
            TraceUtil.endSection();
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            if (!mediaCodecInfo.isFormatSupported(this.inputFormat)) {
                Log.w("MediaCodecRenderer", Util.formatInvariant("Format exceeds selected codec's capabilities [%s, %s]", Format.toLogString(this.inputFormat), str));
            }
            this.codecInfo = mediaCodecInfo;
            this.codecOperatingRate = f;
            this.codecInputFormat = this.inputFormat;
            this.codecAdaptationWorkaroundMode = codecAdaptationWorkaroundMode(str);
            this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(str, this.codecInputFormat);
            this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(str);
            this.codecNeedsSosFlushWorkaround = codecNeedsSosFlushWorkaround(str);
            this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(str);
            this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(str);
            this.codecNeedsEosBufferTimestampWorkaround = codecNeedsEosBufferTimestampWorkaround(str);
            this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(str, this.codecInputFormat);
            this.codecNeedsEosPropagation = codecNeedsEosPropagationWorkaround(mediaCodecInfo) || getCodecNeedsEosPropagation();
            if (this.codec.needsReconfiguration()) {
                this.codecReconfigured = true;
                this.codecReconfigurationState = 1;
                this.codecNeedsAdaptationWorkaroundBuffer = this.codecAdaptationWorkaroundMode != 0;
            }
            if ("c2.android.mp3.decoder".equals(mediaCodecInfo.name)) {
                this.c2Mp3TimestampTracker = new C2Mp3TimestampTracker();
            }
            if (getState() == 2) {
                this.codecHotswapDeadlineMs = SystemClock.elapsedRealtime() + 1000;
            }
            this.decoderCounters.decoderInitCount++;
            onCodecInitialized(str, mediaCodecConfiguration, elapsedRealtime2, elapsedRealtime2 - elapsedRealtime);
        } catch (Throwable th) {
            TraceUtil.endSection();
            throw th;
        }
    }

    private boolean isDecodeOnlyBuffer(long j) {
        int size = this.decodeOnlyPresentationTimestamps.size();
        for (int i = 0; i < size; i++) {
            if (((Long) this.decodeOnlyPresentationTimestamps.get(i)).longValue() == j) {
                this.decodeOnlyPresentationTimestamps.remove(i);
                return true;
            }
        }
        return false;
    }

    private static boolean isMediaCodecException(IllegalStateException illegalStateException) {
        if (Util.SDK_INT >= 21 && isMediaCodecExceptionV21(illegalStateException)) {
            return true;
        }
        StackTraceElement[] stackTrace = illegalStateException.getStackTrace();
        return stackTrace.length > 0 && stackTrace[0].getClassName().equals("android.media.MediaCodec");
    }

    private static boolean isMediaCodecExceptionV21(IllegalStateException illegalStateException) {
        return MediaCodecDecoderException$$ExternalSyntheticApiModelOutline0.m(illegalStateException);
    }

    private static boolean isRecoverableMediaCodecExceptionV21(IllegalStateException illegalStateException) {
        boolean isRecoverable;
        if (!MediaCodecDecoderException$$ExternalSyntheticApiModelOutline0.m(illegalStateException)) {
            return false;
        }
        isRecoverable = MediaCodecDecoderException$$ExternalSyntheticApiModelOutline1.m(illegalStateException).isRecoverable();
        return isRecoverable;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00b0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x004a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void maybeInitCodecWithFallback(MediaCrypto mediaCrypto, boolean z) {
        DecoderInitializationException decoderInitializationException;
        if (this.availableCodecInfos == null) {
            try {
                List availableCodecInfos = getAvailableCodecInfos(z);
                ArrayDeque arrayDeque = new ArrayDeque();
                this.availableCodecInfos = arrayDeque;
                if (this.enableDecoderFallback) {
                    arrayDeque.addAll(availableCodecInfos);
                } else if (!availableCodecInfos.isEmpty()) {
                    this.availableCodecInfos.add((MediaCodecInfo) availableCodecInfos.get(0));
                }
                this.preferredDecoderInitializationException = null;
            } catch (MediaCodecUtil.DecoderQueryException e) {
                throw new DecoderInitializationException(this.inputFormat, e, z, -49998);
            }
        }
        if (this.availableCodecInfos.isEmpty()) {
            throw new DecoderInitializationException(this.inputFormat, (Throwable) null, z, -49999);
        }
        MediaCodecInfo mediaCodecInfo = (MediaCodecInfo) this.availableCodecInfos.peekFirst();
        while (this.codec == null) {
            MediaCodecInfo mediaCodecInfo2 = (MediaCodecInfo) this.availableCodecInfos.peekFirst();
            if (!shouldInitCodec(mediaCodecInfo2)) {
                return;
            }
            try {
                initCodec(mediaCodecInfo2, mediaCrypto);
            } catch (Exception e2) {
                if (mediaCodecInfo2 != mediaCodecInfo) {
                    throw e2;
                    break;
                }
                try {
                    Log.w("MediaCodecRenderer", "Preferred decoder instantiation failed. Sleeping for 50ms then retrying.");
                    Thread.sleep(50L);
                    initCodec(mediaCodecInfo2, mediaCrypto);
                } catch (Exception e3) {
                    Log.w("MediaCodecRenderer", "Failed to initialize decoder: " + mediaCodecInfo2, e3);
                    this.availableCodecInfos.removeFirst();
                    DecoderInitializationException decoderInitializationException2 = new DecoderInitializationException(this.inputFormat, e3, z, mediaCodecInfo2);
                    onCodecError(decoderInitializationException2);
                    decoderInitializationException = this.preferredDecoderInitializationException;
                    if (decoderInitializationException != null) {
                        this.preferredDecoderInitializationException = decoderInitializationException2;
                    } else {
                        this.preferredDecoderInitializationException = decoderInitializationException.copyWithFallbackException(decoderInitializationException2);
                    }
                    if (!this.availableCodecInfos.isEmpty()) {
                        throw this.preferredDecoderInitializationException;
                    }
                }
                Log.w("MediaCodecRenderer", "Failed to initialize decoder: " + mediaCodecInfo2, e3);
                this.availableCodecInfos.removeFirst();
                DecoderInitializationException decoderInitializationException22 = new DecoderInitializationException(this.inputFormat, e3, z, mediaCodecInfo2);
                onCodecError(decoderInitializationException22);
                decoderInitializationException = this.preferredDecoderInitializationException;
                if (decoderInitializationException != null) {
                }
                if (!this.availableCodecInfos.isEmpty()) {
                }
            }
        }
        this.availableCodecInfos = null;
    }

    private void processEndOfStream() {
        int i = this.codecDrainAction;
        if (i == 1) {
            flushCodec();
            return;
        }
        if (i == 2) {
            flushCodec();
            updateDrmSessionV23();
        } else if (i == 3) {
            reinitializeCodec();
        } else {
            this.outputStreamEnded = true;
            renderToEndOfStream();
        }
    }

    private void processOutputMediaFormatChanged() {
        this.codecHasOutputMediaFormat = true;
        MediaFormat outputFormat = this.codec.getOutputFormat();
        if (this.codecAdaptationWorkaroundMode != 0 && outputFormat.getInteger("width") == 32 && outputFormat.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            outputFormat.setInteger("channel-count", 1);
        }
        this.codecOutputMediaFormat = outputFormat;
        this.codecOutputMediaFormatChanged = true;
    }

    private boolean readSourceOmittingSampleData(int i) {
        FormatHolder formatHolder = getFormatHolder();
        this.noDataBuffer.clear();
        int readSource = readSource(formatHolder, this.noDataBuffer, i | 4);
        if (readSource == -5) {
            onInputFormatChanged(formatHolder);
            return true;
        }
        if (readSource != -4 || !this.noDataBuffer.isEndOfStream()) {
            return false;
        }
        this.inputStreamEnded = true;
        processEndOfStream();
        return false;
    }

    private void reinitializeCodec() {
        releaseCodec();
        maybeInitCodecOrBypass();
    }

    private void resetInputBuffer() {
        this.inputIndex = -1;
        this.buffer.data = null;
    }

    private void resetOutputBuffer() {
        this.outputIndex = -1;
        this.outputBuffer = null;
    }

    private void setCodecDrmSession(DrmSession drmSession) {
        DrmSession.-CC.replaceSession(this.codecDrmSession, drmSession);
        this.codecDrmSession = drmSession;
    }

    private void setOutputStreamOffsetUs(long j) {
        this.outputStreamOffsetUs = j;
        if (j != -9223372036854775807L) {
            onOutputStreamOffsetUsChanged(j);
        }
    }

    private void setSourceDrmSession(DrmSession drmSession) {
        DrmSession.-CC.replaceSession(this.sourceDrmSession, drmSession);
        this.sourceDrmSession = drmSession;
    }

    private boolean shouldContinueRendering(long j) {
        return this.renderTimeLimitMs == -9223372036854775807L || SystemClock.elapsedRealtime() - j < this.renderTimeLimitMs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean supportsFormatDrm(Format format) {
        int i = format.cryptoType;
        return i == 0 || i == 2;
    }

    private boolean updateCodecOperatingRate(Format format) {
        if (Util.SDK_INT >= 23 && this.codec != null && this.codecDrainAction != 3 && getState() != 0) {
            float codecOperatingRateV23 = getCodecOperatingRateV23(this.targetPlaybackSpeed, format, getStreamFormats());
            float f = this.codecOperatingRate;
            if (f == codecOperatingRateV23) {
                return true;
            }
            if (codecOperatingRateV23 == -1.0f) {
                drainAndReinitializeCodec();
                return false;
            }
            if (f == -1.0f && codecOperatingRateV23 <= this.assumedMinimumCodecOperatingRate) {
                return true;
            }
            Bundle bundle = new Bundle();
            bundle.putFloat("operating-rate", codecOperatingRateV23);
            this.codec.setParameters(bundle);
            this.codecOperatingRate = codecOperatingRateV23;
        }
        return true;
    }

    private void updateDrmSessionV23() {
        try {
            this.mediaCrypto.setMediaDrmSession(getFrameworkCryptoConfig(this.sourceDrmSession).sessionId);
            setCodecDrmSession(this.sourceDrmSession);
            this.codecDrainState = 0;
            this.codecDrainAction = 0;
        } catch (MediaCryptoException e) {
            throw createRendererException(e, this.inputFormat, 6006);
        }
    }

    protected abstract DecoderReuseEvaluation canReuseCodec(MediaCodecInfo mediaCodecInfo, Format format, Format format2);

    protected MediaCodecDecoderException createDecoderException(Throwable th, MediaCodecInfo mediaCodecInfo) {
        return new MediaCodecDecoderException(th, mediaCodecInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean flushOrReinitializeCodec() {
        boolean flushOrReleaseCodec = flushOrReleaseCodec();
        if (flushOrReleaseCodec) {
            maybeInitCodecOrBypass();
        }
        return flushOrReleaseCodec;
    }

    protected boolean flushOrReleaseCodec() {
        if (this.codec == null) {
            return false;
        }
        int i = this.codecDrainAction;
        if (i == 3 || this.codecNeedsFlushWorkaround || ((this.codecNeedsSosFlushWorkaround && !this.codecHasOutputMediaFormat) || (this.codecNeedsEosFlushWorkaround && this.codecReceivedEos))) {
            releaseCodec();
            return true;
        }
        if (i == 2) {
            int i2 = Util.SDK_INT;
            Assertions.checkState(i2 >= 23);
            if (i2 >= 23) {
                try {
                    updateDrmSessionV23();
                } catch (ExoPlaybackException e) {
                    Log.w("MediaCodecRenderer", "Failed to update the DRM session, releasing the codec instead.", e);
                    releaseCodec();
                    return true;
                }
            }
        }
        flushCodec();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final MediaCodecAdapter getCodec() {
        return this.codec;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final MediaCodecInfo getCodecInfo() {
        return this.codecInfo;
    }

    protected boolean getCodecNeedsEosPropagation() {
        return false;
    }

    protected abstract float getCodecOperatingRateV23(float f, Format format, Format[] formatArr);

    /* JADX INFO: Access modifiers changed from: protected */
    public final MediaFormat getCodecOutputMediaFormat() {
        return this.codecOutputMediaFormat;
    }

    protected abstract List getDecoderInfos(MediaCodecSelector mediaCodecSelector, Format format, boolean z);

    protected abstract MediaCodecAdapter.Configuration getMediaCodecConfiguration(MediaCodecInfo mediaCodecInfo, Format format, MediaCrypto mediaCrypto, float f);

    /* JADX INFO: Access modifiers changed from: protected */
    public final long getOutputStreamOffsetUs() {
        return this.outputStreamOffsetUs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getPlaybackSpeed() {
        return this.currentPlaybackSpeed;
    }

    protected void handleInputBufferSupplementalData(DecoderInputBuffer decoderInputBuffer) {
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        return this.inputFormat != null && (isSourceReady() || hasOutputBuffer() || (this.codecHotswapDeadlineMs != -9223372036854775807L && SystemClock.elapsedRealtime() < this.codecHotswapDeadlineMs));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void maybeInitCodecOrBypass() {
        Format format;
        if (this.codec != null || this.bypassEnabled || (format = this.inputFormat) == null) {
            return;
        }
        if (this.sourceDrmSession == null && shouldUseBypass(format)) {
            initBypass(this.inputFormat);
            return;
        }
        setCodecDrmSession(this.sourceDrmSession);
        String str = this.inputFormat.sampleMimeType;
        DrmSession drmSession = this.codecDrmSession;
        if (drmSession != null) {
            if (this.mediaCrypto == null) {
                FrameworkCryptoConfig frameworkCryptoConfig = getFrameworkCryptoConfig(drmSession);
                if (frameworkCryptoConfig != null) {
                    try {
                        MediaCrypto mediaCrypto = new MediaCrypto(frameworkCryptoConfig.uuid, frameworkCryptoConfig.sessionId);
                        this.mediaCrypto = mediaCrypto;
                        this.mediaCryptoRequiresSecureDecoder = !frameworkCryptoConfig.forceAllowInsecureDecoderComponents && mediaCrypto.requiresSecureDecoderComponent(str);
                    } catch (MediaCryptoException e) {
                        throw createRendererException(e, this.inputFormat, 6006);
                    }
                } else if (this.codecDrmSession.getError() == null) {
                    return;
                }
            }
            if (FrameworkCryptoConfig.WORKAROUND_DEVICE_NEEDS_KEYS_TO_CONFIGURE_CODEC) {
                int state = this.codecDrmSession.getState();
                if (state == 1) {
                    DrmSession.DrmSessionException drmSessionException = (DrmSession.DrmSessionException) Assertions.checkNotNull(this.codecDrmSession.getError());
                    throw createRendererException(drmSessionException, this.inputFormat, drmSessionException.errorCode);
                }
                if (state != 4) {
                    return;
                }
            }
        }
        try {
            maybeInitCodecWithFallback(this.mediaCrypto, this.mediaCryptoRequiresSecureDecoder);
        } catch (DecoderInitializationException e2) {
            throw createRendererException(e2, this.inputFormat, 4001);
        }
    }

    protected abstract void onCodecError(Exception exc);

    protected abstract void onCodecInitialized(String str, MediaCodecAdapter.Configuration configuration, long j, long j2);

    protected abstract void onCodecReleased(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onDisabled() {
        this.inputFormat = null;
        this.outputStreamStartPositionUs = -9223372036854775807L;
        setOutputStreamOffsetUs(-9223372036854775807L);
        this.pendingOutputStreamOffsetCount = 0;
        flushOrReleaseCodec();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onEnabled(boolean z, boolean z2) {
        this.decoderCounters = new DecoderCounters();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007e, code lost:
    
        if (drainAndUpdateCodecDrmSessionV23() == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00cf, code lost:
    
        r7 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00b0, code lost:
    
        if (drainAndUpdateCodecDrmSessionV23() == false) goto L69;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DecoderReuseEvaluation onInputFormatChanged(FormatHolder formatHolder) {
        int i;
        boolean z = true;
        this.waitingForFirstSampleInFormat = true;
        Format format = (Format) Assertions.checkNotNull(formatHolder.format);
        if (format.sampleMimeType == null) {
            throw createRendererException(new IllegalArgumentException(), format, 4005);
        }
        setSourceDrmSession(formatHolder.drmSession);
        this.inputFormat = format;
        if (this.bypassEnabled) {
            this.bypassDrainAndReinitialize = true;
            return null;
        }
        MediaCodecAdapter mediaCodecAdapter = this.codec;
        if (mediaCodecAdapter == null) {
            this.availableCodecInfos = null;
            maybeInitCodecOrBypass();
            return null;
        }
        MediaCodecInfo mediaCodecInfo = this.codecInfo;
        Format format2 = this.codecInputFormat;
        if (drmNeedsCodecReinitialization(mediaCodecInfo, format, this.codecDrmSession, this.sourceDrmSession)) {
            drainAndReinitializeCodec();
            return new DecoderReuseEvaluation(mediaCodecInfo.name, format2, format, 0, 128);
        }
        boolean z2 = this.sourceDrmSession != this.codecDrmSession;
        Assertions.checkState(!z2 || Util.SDK_INT >= 23);
        DecoderReuseEvaluation canReuseCodec = canReuseCodec(mediaCodecInfo, format2, format);
        int i2 = canReuseCodec.result;
        if (i2 != 0) {
            if (i2 == 1) {
                if (updateCodecOperatingRate(format)) {
                    this.codecInputFormat = format;
                    if (!z2) {
                    }
                }
                i = 16;
            } else if (i2 == 2) {
                if (updateCodecOperatingRate(format)) {
                    this.codecReconfigured = true;
                    this.codecReconfigurationState = 1;
                    int i3 = this.codecAdaptationWorkaroundMode;
                    if (i3 != 2 && (i3 != 1 || format.width != format2.width || format.height != format2.height)) {
                        z = false;
                    }
                    this.codecNeedsAdaptationWorkaroundBuffer = z;
                    this.codecInputFormat = format;
                    if (z2) {
                    }
                }
                i = 16;
            } else {
                if (i2 != 3) {
                    throw new IllegalStateException();
                }
                if (updateCodecOperatingRate(format)) {
                    this.codecInputFormat = format;
                    if (z2) {
                    }
                }
                i = 16;
            }
            return (canReuseCodec.result != 0 || (this.codec == mediaCodecAdapter && this.codecDrainAction != 3)) ? canReuseCodec : new DecoderReuseEvaluation(mediaCodecInfo.name, format2, format, 0, i);
        }
        drainAndReinitializeCodec();
        i = 0;
        if (canReuseCodec.result != 0) {
        }
    }

    protected abstract void onOutputFormatChanged(Format format, MediaFormat mediaFormat);

    protected void onOutputStreamOffsetUsChanged(long j) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onPositionReset(long j, boolean z) {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        this.pendingOutputEndOfStream = false;
        if (this.bypassEnabled) {
            this.bypassBatchBuffer.clear();
            this.bypassSampleBuffer.clear();
            this.bypassSampleBufferPending = false;
        } else {
            flushOrReinitializeCodec();
        }
        if (this.formatQueue.size() > 0) {
            this.waitingForFirstSampleInFormat = true;
        }
        this.formatQueue.clear();
        int i = this.pendingOutputStreamOffsetCount;
        if (i != 0) {
            setOutputStreamOffsetUs(this.pendingOutputStreamOffsetsUs[i - 1]);
            this.outputStreamStartPositionUs = this.pendingOutputStreamStartPositionsUs[this.pendingOutputStreamOffsetCount - 1];
            this.pendingOutputStreamOffsetCount = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onProcessedOutputBuffer(long j) {
        while (this.pendingOutputStreamOffsetCount != 0 && j >= this.pendingOutputStreamSwitchTimesUs[0]) {
            this.outputStreamStartPositionUs = this.pendingOutputStreamStartPositionsUs[0];
            setOutputStreamOffsetUs(this.pendingOutputStreamOffsetsUs[0]);
            int i = this.pendingOutputStreamOffsetCount - 1;
            this.pendingOutputStreamOffsetCount = i;
            long[] jArr = this.pendingOutputStreamStartPositionsUs;
            System.arraycopy(jArr, 1, jArr, 0, i);
            long[] jArr2 = this.pendingOutputStreamOffsetsUs;
            System.arraycopy(jArr2, 1, jArr2, 0, this.pendingOutputStreamOffsetCount);
            long[] jArr3 = this.pendingOutputStreamSwitchTimesUs;
            System.arraycopy(jArr3, 1, jArr3, 0, this.pendingOutputStreamOffsetCount);
            onProcessedStreamChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onProcessedStreamChange() {
    }

    protected abstract void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onReset() {
        try {
            disableBypass();
            releaseCodec();
        } finally {
            setSourceDrmSession(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onStarted() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onStopped() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.BaseRenderer
    public void onStreamChanged(Format[] formatArr, long j, long j2) {
        if (this.outputStreamOffsetUs == -9223372036854775807L) {
            Assertions.checkState(this.outputStreamStartPositionUs == -9223372036854775807L);
            this.outputStreamStartPositionUs = j;
            setOutputStreamOffsetUs(j2);
            return;
        }
        int i = this.pendingOutputStreamOffsetCount;
        if (i == this.pendingOutputStreamOffsetsUs.length) {
            Log.w("MediaCodecRenderer", "Too many stream changes, so dropping offset: " + this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
        } else {
            this.pendingOutputStreamOffsetCount = i + 1;
        }
        long[] jArr = this.pendingOutputStreamStartPositionsUs;
        int i2 = this.pendingOutputStreamOffsetCount - 1;
        jArr[i2] = j;
        this.pendingOutputStreamOffsetsUs[i2] = j2;
        this.pendingOutputStreamSwitchTimesUs[i2] = this.largestQueuedPresentationTimeUs;
    }

    protected abstract boolean processOutputBuffer(long j, long j2, MediaCodecAdapter mediaCodecAdapter, ByteBuffer byteBuffer, int i, int i2, int i3, long j3, boolean z, boolean z2, Format format);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public void releaseCodec() {
        try {
            MediaCodecAdapter mediaCodecAdapter = this.codec;
            if (mediaCodecAdapter != null) {
                mediaCodecAdapter.release();
                this.decoderCounters.decoderReleaseCount++;
                onCodecReleased(this.codecInfo.name);
            }
            this.codec = null;
            try {
                MediaCrypto mediaCrypto = this.mediaCrypto;
                if (mediaCrypto != null) {
                    mediaCrypto.release();
                }
            } finally {
            }
        } catch (Throwable th) {
            this.codec = null;
            try {
                MediaCrypto mediaCrypto2 = this.mediaCrypto;
                if (mediaCrypto2 != null) {
                    mediaCrypto2.release();
                }
                throw th;
            } finally {
            }
        }
    }

    @Override // com.google.android.exoplayer2.Renderer
    public void render(long j, long j2) {
        boolean z = false;
        if (this.pendingOutputEndOfStream) {
            this.pendingOutputEndOfStream = false;
            processEndOfStream();
        }
        ExoPlaybackException exoPlaybackException = this.pendingPlaybackException;
        if (exoPlaybackException != null) {
            this.pendingPlaybackException = null;
            throw exoPlaybackException;
        }
        try {
            if (this.outputStreamEnded) {
                renderToEndOfStream();
                return;
            }
            if (this.inputFormat != null || readSourceOmittingSampleData(2)) {
                maybeInitCodecOrBypass();
                if (this.bypassEnabled) {
                    TraceUtil.beginSection("bypassRender");
                    do {
                    } while (bypassRender(j, j2));
                } else {
                    if (this.codec == null) {
                        this.decoderCounters.skippedInputBufferCount += skipSource(j);
                        readSourceOmittingSampleData(1);
                        this.decoderCounters.ensureUpdated();
                    }
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    TraceUtil.beginSection("drainAndFeed");
                    while (drainOutputBuffer(j, j2) && shouldContinueRendering(elapsedRealtime)) {
                    }
                    while (feedInputBuffer() && shouldContinueRendering(elapsedRealtime)) {
                    }
                }
                TraceUtil.endSection();
                this.decoderCounters.ensureUpdated();
            }
        } catch (IllegalStateException e) {
            if (!isMediaCodecException(e)) {
                throw e;
            }
            onCodecError(e);
            if (Util.SDK_INT >= 21 && isRecoverableMediaCodecExceptionV21(e)) {
                z = true;
            }
            if (z) {
                releaseCodec();
            }
            throw createRendererException(createDecoderException(e, getCodecInfo()), this.inputFormat, z, 4003);
        }
    }

    protected void renderToEndOfStream() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetCodecStateForFlush() {
        resetInputBuffer();
        resetOutputBuffer();
        this.codecHotswapDeadlineMs = -9223372036854775807L;
        this.codecReceivedEos = false;
        this.codecReceivedBuffers = false;
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        this.isDecodeOnlyOutputBuffer = false;
        this.isLastOutputBuffer = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.largestQueuedPresentationTimeUs = -9223372036854775807L;
        this.lastBufferInStreamPresentationTimeUs = -9223372036854775807L;
        C2Mp3TimestampTracker c2Mp3TimestampTracker = this.c2Mp3TimestampTracker;
        if (c2Mp3TimestampTracker != null) {
            c2Mp3TimestampTracker.reset();
        }
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.codecReconfigurationState = this.codecReconfigured ? 1 : 0;
    }

    protected void resetCodecStateForRelease() {
        resetCodecStateForFlush();
        this.pendingPlaybackException = null;
        this.c2Mp3TimestampTracker = null;
        this.availableCodecInfos = null;
        this.codecInfo = null;
        this.codecInputFormat = null;
        this.codecOutputMediaFormat = null;
        this.codecOutputMediaFormatChanged = false;
        this.codecHasOutputMediaFormat = false;
        this.codecOperatingRate = -1.0f;
        this.codecAdaptationWorkaroundMode = 0;
        this.codecNeedsDiscardToSpsWorkaround = false;
        this.codecNeedsFlushWorkaround = false;
        this.codecNeedsSosFlushWorkaround = false;
        this.codecNeedsEosFlushWorkaround = false;
        this.codecNeedsEosOutputExceptionWorkaround = false;
        this.codecNeedsEosBufferTimestampWorkaround = false;
        this.codecNeedsMonoChannelCountWorkaround = false;
        this.codecNeedsEosPropagation = false;
        this.codecReconfigured = false;
        this.codecReconfigurationState = 0;
        this.mediaCryptoRequiresSecureDecoder = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setPendingOutputEndOfStream() {
        this.pendingOutputEndOfStream = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setPendingPlaybackException(ExoPlaybackException exoPlaybackException) {
        this.pendingPlaybackException = exoPlaybackException;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.Renderer
    public void setPlaybackSpeed(float f, float f2) {
        this.currentPlaybackSpeed = f;
        this.targetPlaybackSpeed = f2;
        updateCodecOperatingRate(this.codecInputFormat);
    }

    protected boolean shouldInitCodec(MediaCodecInfo mediaCodecInfo) {
        return true;
    }

    protected boolean shouldReinitCodec() {
        return false;
    }

    protected boolean shouldUseBypass(Format format) {
        return false;
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    public final int supportsFormat(Format format) {
        try {
            return supportsFormat(this.mediaCodecSelector, format);
        } catch (MediaCodecUtil.DecoderQueryException e) {
            throw createRendererException(e, format, 4002);
        }
    }

    protected abstract int supportsFormat(MediaCodecSelector mediaCodecSelector, Format format);

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.RendererCapabilities
    public final int supportsMixedMimeTypeAdaptation() {
        return 8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void updateOutputFormatForTime(long j) {
        Format format = (Format) this.formatQueue.pollFloor(j);
        if (format == null && this.codecOutputMediaFormatChanged) {
            format = (Format) this.formatQueue.pollFirst();
        }
        if (format != null) {
            this.outputFormat = format;
        } else if (!this.codecOutputMediaFormatChanged || this.outputFormat == null) {
            return;
        }
        onOutputFormatChanged(this.outputFormat, this.codecOutputMediaFormat);
        this.codecOutputMediaFormatChanged = false;
    }
}
