package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import android.view.Display;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecDecoderException;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MediaFormatUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.voip.VoIPService;
/* loaded from: classes.dex */
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = {1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static boolean deviceNeedsSetOutputSurfaceWorkaround;
    private static boolean evaluatedDeviceNeedsSetOutputSurfaceWorkaround;
    private final long allowedJoiningTimeMs;
    private int buffersInCodecCount;
    private boolean codecHandlesHdr10PlusOutOfBandMetadata;
    private CodecMaxValues codecMaxValues;
    private boolean codecNeedsSetOutputSurfaceWorkaround;
    private int consecutiveDroppedFrameCount;
    private final Context context;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private final boolean deviceNeedsNoPostProcessWorkaround;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private final VideoRendererEventListener.EventDispatcher eventDispatcher;
    private VideoFrameMetadataListener frameMetadataListener;
    private final VideoFrameReleaseHelper frameReleaseHelper;
    private boolean haveReportedFirstFrameRenderedForCurrentSurface;
    private long initialPositionUs;
    private long joiningDeadlineMs;
    private long lastBufferPresentationTimeUs;
    private long lastRenderRealtimeUs;
    private final int maxDroppedFramesToNotify;
    private boolean mayRenderFirstFrameAfterEnableIfNotStarted;
    private PlaceholderSurface placeholderSurface;
    private boolean renderedFirstFrameAfterEnable;
    private boolean renderedFirstFrameAfterReset;
    private VideoSize reportedVideoSize;
    private int scalingMode;
    private Surface surface;
    private long totalVideoFrameProcessingOffsetUs;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;
    private int videoFrameProcessingOffsetCount;

    private static boolean isBufferLate(long j) {
        return j < -30000;
    }

    private static boolean isBufferVeryLate(long j) {
        return j < -500000;
    }

    @Override // com.google.android.exoplayer2.Renderer, com.google.android.exoplayer2.RendererCapabilities
    public String getName() {
        return "MediaCodecVideoRenderer";
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecAdapter.Factory factory, MediaCodecSelector mediaCodecSelector, long j, boolean z, Handler handler, VideoRendererEventListener videoRendererEventListener, int i) {
        this(context, factory, mediaCodecSelector, j, z, handler, videoRendererEventListener, i, 30.0f);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecAdapter.Factory factory, MediaCodecSelector mediaCodecSelector, long j, boolean z, Handler handler, VideoRendererEventListener videoRendererEventListener, int i, float f) {
        super(2, factory, mediaCodecSelector, z, f);
        this.allowedJoiningTimeMs = j;
        this.maxDroppedFramesToNotify = i;
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.frameReleaseHelper = new VideoFrameReleaseHelper(applicationContext);
        this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(handler, videoRendererEventListener);
        this.deviceNeedsNoPostProcessWorkaround = deviceNeedsNoPostProcessWorkaround();
        this.joiningDeadlineMs = -9223372036854775807L;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.scalingMode = 1;
        this.tunnelingAudioSessionId = 0;
        clearReportedVideoSize();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, Format format) throws MediaCodecUtil.DecoderQueryException {
        boolean z;
        int i = 0;
        if (!MimeTypes.isVideo(format.sampleMimeType)) {
            return RendererCapabilities.-CC.create(0);
        }
        boolean z2 = format.drmInitData != null;
        List<MediaCodecInfo> decoderInfos = getDecoderInfos(this.context, mediaCodecSelector, format, z2, false);
        if (z2 && decoderInfos.isEmpty()) {
            decoderInfos = getDecoderInfos(this.context, mediaCodecSelector, format, false, false);
        }
        if (decoderInfos.isEmpty()) {
            return RendererCapabilities.-CC.create(1);
        }
        if (!MediaCodecRenderer.supportsFormatDrm(format)) {
            return RendererCapabilities.-CC.create(2);
        }
        MediaCodecInfo mediaCodecInfo = decoderInfos.get(0);
        boolean isFormatSupported = mediaCodecInfo.isFormatSupported(format);
        if (!isFormatSupported) {
            for (int i2 = 1; i2 < decoderInfos.size(); i2++) {
                MediaCodecInfo mediaCodecInfo2 = decoderInfos.get(i2);
                if (mediaCodecInfo2.isFormatSupported(format)) {
                    mediaCodecInfo = mediaCodecInfo2;
                    z = false;
                    isFormatSupported = true;
                    break;
                }
            }
        }
        z = true;
        int i3 = isFormatSupported ? 4 : 3;
        int i4 = mediaCodecInfo.isSeamlessAdaptationSupported(format) ? 16 : 8;
        int i5 = mediaCodecInfo.hardwareAccelerated ? 64 : 0;
        int i6 = z ? 128 : 0;
        if (Util.SDK_INT >= 26 && "video/dolby-vision".equals(format.sampleMimeType) && !Api26.doesDisplaySupportDolbyVision(this.context)) {
            i6 = LiteMode.FLAG_CHAT_BLUR;
        }
        if (isFormatSupported) {
            List<MediaCodecInfo> decoderInfos2 = getDecoderInfos(this.context, mediaCodecSelector, format, z2, true);
            if (!decoderInfos2.isEmpty()) {
                MediaCodecInfo mediaCodecInfo3 = MediaCodecUtil.getDecoderInfosSortedByFormatSupport(decoderInfos2, format).get(0);
                if (mediaCodecInfo3.isFormatSupported(format) && mediaCodecInfo3.isSeamlessAdaptationSupported(format)) {
                    i = 32;
                }
            }
        }
        return RendererCapabilities.-CC.create(i3, i4, i, i5, i6);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector, Format format, boolean z) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfosSortedByFormatSupport(getDecoderInfos(this.context, mediaCodecSelector, format, z, this.tunneling), format);
    }

    private static List<MediaCodecInfo> getDecoderInfos(Context context, MediaCodecSelector mediaCodecSelector, Format format, boolean z, boolean z2) throws MediaCodecUtil.DecoderQueryException {
        String str = format.sampleMimeType;
        if (str == null) {
            return ImmutableList.of();
        }
        List<MediaCodecInfo> decoderInfos = mediaCodecSelector.getDecoderInfos(str, z, z2);
        String alternativeCodecMimeType = MediaCodecUtil.getAlternativeCodecMimeType(format);
        if (alternativeCodecMimeType == null) {
            return ImmutableList.copyOf((Collection) decoderInfos);
        }
        List<MediaCodecInfo> decoderInfos2 = mediaCodecSelector.getDecoderInfos(alternativeCodecMimeType, z, z2);
        if (Util.SDK_INT >= 26 && "video/dolby-vision".equals(format.sampleMimeType) && !decoderInfos2.isEmpty() && !Api26.doesDisplaySupportDolbyVision(context)) {
            return ImmutableList.copyOf((Collection) decoderInfos2);
        }
        return ImmutableList.builder().addAll((Iterable) decoderInfos).addAll((Iterable) decoderInfos2).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Api26 {
        public static boolean doesDisplaySupportDolbyVision(Context context) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            Display display = displayManager != null ? displayManager.getDisplay(0) : null;
            if (display == null || !display.isHdr()) {
                return false;
            }
            for (int i : display.getHdrCapabilities().getSupportedHdrTypes()) {
                if (i == 1) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    public void onEnabled(boolean z, boolean z2) throws ExoPlaybackException {
        super.onEnabled(z, z2);
        boolean z3 = getConfiguration().tunneling;
        Assertions.checkState((z3 && this.tunnelingAudioSessionId == 0) ? false : true);
        if (this.tunneling != z3) {
            this.tunneling = z3;
            releaseCodec();
        }
        this.eventDispatcher.enabled(this.decoderCounters);
        this.mayRenderFirstFrameAfterEnableIfNotStarted = z2;
        this.renderedFirstFrameAfterEnable = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    public void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        super.onPositionReset(j, z);
        clearRenderedFirstFrame();
        this.frameReleaseHelper.onPositionReset();
        this.lastBufferPresentationTimeUs = -9223372036854775807L;
        this.initialPositionUs = -9223372036854775807L;
        this.consecutiveDroppedFrameCount = 0;
        if (z) {
            setJoiningDeadlineMs();
        } else {
            this.joiningDeadlineMs = -9223372036854775807L;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        PlaceholderSurface placeholderSurface;
        if (super.isReady() && (this.renderedFirstFrameAfterReset || (((placeholderSurface = this.placeholderSurface) != null && this.surface == placeholderSurface) || getCodec() == null || this.tunneling))) {
            this.joiningDeadlineMs = -9223372036854775807L;
            return true;
        } else if (this.joiningDeadlineMs == -9223372036854775807L) {
            return false;
        } else {
            if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
                return true;
            }
            this.joiningDeadlineMs = -9223372036854775807L;
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    public void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderRealtimeUs = SystemClock.elapsedRealtime() * 1000;
        this.totalVideoFrameProcessingOffsetUs = 0L;
        this.videoFrameProcessingOffsetCount = 0;
        this.frameReleaseHelper.onStarted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    public void onStopped() {
        this.joiningDeadlineMs = -9223372036854775807L;
        maybeNotifyDroppedFrames();
        maybeNotifyVideoFrameProcessingOffset();
        this.frameReleaseHelper.onStopped();
        super.onStopped();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    public void onDisabled() {
        clearReportedVideoSize();
        clearRenderedFirstFrame();
        this.haveReportedFirstFrameRenderedForCurrentSurface = false;
        this.tunnelingOnFrameRenderedListener = null;
        try {
            super.onDisabled();
        } finally {
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    @TargetApi(17)
    public void onReset() {
        try {
            super.onReset();
        } finally {
            if (this.placeholderSurface != null) {
                releasePlaceholderSurface();
            }
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.PlayerMessage.Target
    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 1) {
            setOutput(obj);
        } else if (i == 7) {
            this.frameMetadataListener = (VideoFrameMetadataListener) obj;
        } else if (i == 10) {
            int intValue = ((Integer) obj).intValue();
            if (this.tunnelingAudioSessionId != intValue) {
                this.tunnelingAudioSessionId = intValue;
                if (this.tunneling) {
                    releaseCodec();
                }
            }
        } else if (i != 4) {
            if (i == 5) {
                this.frameReleaseHelper.setChangeFrameRateStrategy(((Integer) obj).intValue());
            } else {
                super.handleMessage(i, obj);
            }
        } else {
            this.scalingMode = ((Integer) obj).intValue();
            MediaCodecAdapter codec = getCodec();
            if (codec != null) {
                codec.setVideoScalingMode(this.scalingMode);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.video.MediaCodecVideoRenderer] */
    /* JADX WARN: Type inference failed for: r5v9, types: [android.view.Surface] */
    private void setOutput(Object obj) throws ExoPlaybackException {
        PlaceholderSurface placeholderSurface = obj instanceof Surface ? (Surface) obj : null;
        if (placeholderSurface == null) {
            PlaceholderSurface placeholderSurface2 = this.placeholderSurface;
            if (placeholderSurface2 != null) {
                placeholderSurface = placeholderSurface2;
            } else {
                MediaCodecInfo codecInfo = getCodecInfo();
                if (codecInfo != null && shouldUsePlaceholderSurface(codecInfo)) {
                    placeholderSurface = PlaceholderSurface.newInstanceV17(this.context, codecInfo.secure);
                    this.placeholderSurface = placeholderSurface;
                }
            }
        }
        if (this.surface != placeholderSurface) {
            this.surface = placeholderSurface;
            this.frameReleaseHelper.onSurfaceChanged(placeholderSurface);
            this.haveReportedFirstFrameRenderedForCurrentSurface = false;
            int state = getState();
            MediaCodecAdapter codec = getCodec();
            if (codec != null) {
                if (Util.SDK_INT >= 23 && placeholderSurface != null && !this.codecNeedsSetOutputSurfaceWorkaround) {
                    try {
                        setOutputSurfaceV23(codec, placeholderSurface);
                    } catch (Throwable th) {
                        throw new SurfaceNotValidException(th);
                    }
                } else {
                    releaseCodec();
                    maybeInitCodecOrBypass();
                }
            }
            if (placeholderSurface != null && placeholderSurface != this.placeholderSurface) {
                maybeRenotifyVideoSizeChanged();
                clearRenderedFirstFrame();
                if (state == 2) {
                    setJoiningDeadlineMs();
                    return;
                }
                return;
            }
            clearReportedVideoSize();
            clearRenderedFirstFrame();
        } else if (placeholderSurface == null || placeholderSurface == this.placeholderSurface) {
        } else {
            maybeRenotifyVideoSizeChanged();
            maybeRenotifyRenderedFirstFrame();
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean shouldInitCodec(MediaCodecInfo mediaCodecInfo) {
        return this.surface != null || shouldUsePlaceholderSurface(mediaCodecInfo);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean getCodecNeedsEosPropagation() {
        return this.tunneling && Util.SDK_INT < 23;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    @TargetApi(17)
    protected MediaCodecAdapter.Configuration getMediaCodecConfiguration(MediaCodecInfo mediaCodecInfo, Format format, MediaCrypto mediaCrypto, float f) {
        PlaceholderSurface placeholderSurface = this.placeholderSurface;
        if (placeholderSurface != null && placeholderSurface.secure != mediaCodecInfo.secure) {
            releasePlaceholderSurface();
        }
        String str = mediaCodecInfo.codecMimeType;
        CodecMaxValues codecMaxValues = getCodecMaxValues(mediaCodecInfo, format, getStreamFormats());
        this.codecMaxValues = codecMaxValues;
        MediaFormat mediaFormat = getMediaFormat(format, str, codecMaxValues, f, this.deviceNeedsNoPostProcessWorkaround, this.tunneling ? this.tunnelingAudioSessionId : 0);
        if (this.surface == null) {
            if (!shouldUsePlaceholderSurface(mediaCodecInfo)) {
                throw new IllegalStateException();
            }
            if (this.placeholderSurface == null) {
                this.placeholderSurface = PlaceholderSurface.newInstanceV17(this.context, mediaCodecInfo.secure);
            }
            this.surface = this.placeholderSurface;
        }
        return MediaCodecAdapter.Configuration.createForVideoDecoding(mediaCodecInfo, mediaFormat, format, this.surface, mediaCrypto);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected DecoderReuseEvaluation canReuseCodec(MediaCodecInfo mediaCodecInfo, Format format, Format format2) {
        DecoderReuseEvaluation canReuseCodec = mediaCodecInfo.canReuseCodec(format, format2);
        int i = canReuseCodec.discardReasons;
        int i2 = format2.width;
        CodecMaxValues codecMaxValues = this.codecMaxValues;
        if (i2 > codecMaxValues.width || format2.height > codecMaxValues.height) {
            i |= LiteMode.FLAG_CHAT_BLUR;
        }
        if (getMaxInputSize(mediaCodecInfo, format2) > this.codecMaxValues.inputSize) {
            i |= 64;
        }
        int i3 = i;
        return new DecoderReuseEvaluation(mediaCodecInfo.name, format, format2, i3 != 0 ? 0 : canReuseCodec.result, i3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void resetCodecStateForFlush() {
        super.resetCodecStateForFlush();
        this.buffersInCodecCount = 0;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.Renderer
    public void setPlaybackSpeed(float f, float f2) throws ExoPlaybackException {
        super.setPlaybackSpeed(f, f2);
        this.frameReleaseHelper.onPlaybackSpeed(f);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0081, code lost:
        if (r3.equals("video/av01") == false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int getCodecMaxInputSize(MediaCodecInfo mediaCodecInfo, Format format) {
        int intValue;
        int i = format.width;
        int i2 = format.height;
        if (i == -1 || i2 == -1) {
            return -1;
        }
        String str = format.sampleMimeType;
        char c = 1;
        if ("video/dolby-vision".equals(str)) {
            Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format);
            str = (codecProfileAndLevel == null || !((intValue = ((Integer) codecProfileAndLevel.first).intValue()) == 512 || intValue == 1 || intValue == 2)) ? "video/hevc" : MediaController.VIDEO_MIME_TYPE;
        }
        str.hashCode();
        switch (str.hashCode()) {
            case -1664118616:
                if (str.equals("video/3gpp")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1662735862:
                break;
            case -1662541442:
                if (str.equals("video/hevc")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1187890754:
                if (str.equals("video/mp4v-es")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1331836730:
                if (str.equals(MediaController.VIDEO_MIME_TYPE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1599127256:
                if (str.equals("video/x-vnd.on2.vp8")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1599127257:
                if (str.equals("video/x-vnd.on2.vp9")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 3:
            case 5:
                return getMaxSampleSize(i * i2, 2);
            case 2:
                return Math.max(2097152, getMaxSampleSize(i * i2, 2));
            case 4:
                String str2 = Util.MODEL;
                if ("BRAVIA 4K 2015".equals(str2) || ("Amazon".equals(Util.MANUFACTURER) && ("KFSOWI".equals(str2) || ("AFTS".equals(str2) && mediaCodecInfo.secure)))) {
                    return -1;
                }
                return getMaxSampleSize(Util.ceilDivide(i, 16) * Util.ceilDivide(i2, 16) * 16 * 16, 2);
            case 6:
                return getMaxSampleSize(i * i2, 4);
            default:
                return -1;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected float getCodecOperatingRateV23(float f, Format format, Format[] formatArr) {
        float f2 = -1.0f;
        for (Format format2 : formatArr) {
            float f3 = format2.frameRate;
            if (f3 != -1.0f) {
                f2 = Math.max(f2, f3);
            }
        }
        if (f2 == -1.0f) {
            return -1.0f;
        }
        return f2 * f;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onCodecInitialized(String str, MediaCodecAdapter.Configuration configuration, long j, long j2) {
        this.eventDispatcher.decoderInitialized(str, j, j2);
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround(str);
        this.codecHandlesHdr10PlusOutOfBandMetadata = ((MediaCodecInfo) Assertions.checkNotNull(getCodecInfo())).isHdr10PlusOutOfBandMetadataSupported();
        if (Util.SDK_INT < 23 || !this.tunneling) {
            return;
        }
        this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23((MediaCodecAdapter) Assertions.checkNotNull(getCodec()));
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onCodecReleased(String str) {
        this.eventDispatcher.decoderReleased(str);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onCodecError(Exception exc) {
        Log.e("MediaCodecVideoRenderer", "Video codec error", exc);
        this.eventDispatcher.videoCodecError(exc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public DecoderReuseEvaluation onInputFormatChanged(FormatHolder formatHolder) throws ExoPlaybackException {
        DecoderReuseEvaluation onInputFormatChanged = super.onInputFormatChanged(formatHolder);
        this.eventDispatcher.inputFormatChanged(formatHolder.format, onInputFormatChanged);
        return onInputFormatChanged;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer) throws ExoPlaybackException {
        boolean z = this.tunneling;
        if (!z) {
            this.buffersInCodecCount++;
        }
        if (Util.SDK_INT >= 23 || !z) {
            return;
        }
        onProcessedTunneledBuffer(decoderInputBuffer.timeUs);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onOutputFormatChanged(Format format, MediaFormat mediaFormat) {
        int integer;
        int integer2;
        MediaCodecAdapter codec = getCodec();
        if (codec != null) {
            codec.setVideoScalingMode(this.scalingMode);
        }
        if (this.tunneling) {
            this.currentWidth = format.width;
            this.currentHeight = format.height;
        } else {
            Assertions.checkNotNull(mediaFormat);
            boolean z = mediaFormat.containsKey("crop-right") && mediaFormat.containsKey("crop-left") && mediaFormat.containsKey("crop-bottom") && mediaFormat.containsKey("crop-top");
            if (z) {
                integer = (mediaFormat.getInteger("crop-right") - mediaFormat.getInteger("crop-left")) + 1;
            } else {
                integer = mediaFormat.getInteger("width");
            }
            this.currentWidth = integer;
            if (z) {
                integer2 = (mediaFormat.getInteger("crop-bottom") - mediaFormat.getInteger("crop-top")) + 1;
            } else {
                integer2 = mediaFormat.getInteger("height");
            }
            this.currentHeight = integer2;
        }
        float f = format.pixelWidthHeightRatio;
        this.currentPixelWidthHeightRatio = f;
        if (Util.SDK_INT >= 21) {
            int i = format.rotationDegrees;
            if (i == 90 || i == 270) {
                int i2 = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = i2;
                this.currentPixelWidthHeightRatio = 1.0f / f;
            }
        } else {
            this.currentUnappliedRotationDegrees = format.rotationDegrees;
        }
        this.frameReleaseHelper.onFormatChanged(format.frameRate);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    @TargetApi(29)
    protected void handleInputBufferSupplementalData(DecoderInputBuffer decoderInputBuffer) throws ExoPlaybackException {
        if (this.codecHandlesHdr10PlusOutOfBandMetadata) {
            ByteBuffer byteBuffer = (ByteBuffer) Assertions.checkNotNull(decoderInputBuffer.supplementalData);
            if (byteBuffer.remaining() >= 7) {
                byte b = byteBuffer.get();
                short s = byteBuffer.getShort();
                short s2 = byteBuffer.getShort();
                byte b2 = byteBuffer.get();
                byte b3 = byteBuffer.get();
                byteBuffer.position(0);
                if (b == -75 && s == 60 && s2 == 1 && b2 == 4) {
                    if (b3 == 0 || b3 == 1) {
                        byte[] bArr = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bArr);
                        byteBuffer.position(0);
                        setHdr10PlusInfoV29(getCodec(), bArr);
                    }
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean processOutputBuffer(long j, long j2, MediaCodecAdapter mediaCodecAdapter, ByteBuffer byteBuffer, int i, int i2, int i3, long j3, boolean z, boolean z2, Format format) throws ExoPlaybackException {
        long j4;
        boolean z3;
        Assertions.checkNotNull(mediaCodecAdapter);
        if (this.initialPositionUs == -9223372036854775807L) {
            this.initialPositionUs = j;
        }
        if (j3 != this.lastBufferPresentationTimeUs) {
            this.frameReleaseHelper.onNextFrame(j3);
            this.lastBufferPresentationTimeUs = j3;
        }
        long outputStreamOffsetUs = getOutputStreamOffsetUs();
        long j5 = j3 - outputStreamOffsetUs;
        if (z && !z2) {
            skipOutputBuffer(mediaCodecAdapter, i, j5);
            return true;
        }
        double playbackSpeed = getPlaybackSpeed();
        boolean z4 = getState() == 2;
        long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
        double d = j3 - j;
        Double.isNaN(d);
        Double.isNaN(playbackSpeed);
        long j6 = (long) (d / playbackSpeed);
        if (z4) {
            j6 -= elapsedRealtime - j2;
        }
        if (this.surface == this.placeholderSurface) {
            if (isBufferLate(j6)) {
                skipOutputBuffer(mediaCodecAdapter, i, j5);
                updateVideoFrameProcessingOffsetCounters(j6);
                return true;
            }
            return false;
        }
        long j7 = elapsedRealtime - this.lastRenderRealtimeUs;
        if (this.renderedFirstFrameAfterEnable ? this.renderedFirstFrameAfterReset : !(z4 || this.mayRenderFirstFrameAfterEnableIfNotStarted)) {
            j4 = j7;
            z3 = false;
        } else {
            j4 = j7;
            z3 = true;
        }
        if (this.joiningDeadlineMs == -9223372036854775807L && j >= outputStreamOffsetUs && (z3 || (z4 && shouldForceRenderOutputBuffer(j6, j4)))) {
            long nanoTime = System.nanoTime();
            notifyFrameMetadataListener(j5, nanoTime, format);
            if (Util.SDK_INT >= 21) {
                renderOutputBufferV21(mediaCodecAdapter, i, j5, nanoTime);
            } else {
                renderOutputBuffer(mediaCodecAdapter, i, j5);
            }
            updateVideoFrameProcessingOffsetCounters(j6);
            return true;
        }
        if (z4 && j != this.initialPositionUs) {
            long nanoTime2 = System.nanoTime();
            long adjustReleaseTime = this.frameReleaseHelper.adjustReleaseTime((j6 * 1000) + nanoTime2);
            long j8 = (adjustReleaseTime - nanoTime2) / 1000;
            boolean z5 = this.joiningDeadlineMs != -9223372036854775807L;
            if (shouldDropBuffersToKeyframe(j8, j2, z2) && maybeDropBuffersToKeyframe(j, z5)) {
                return false;
            }
            if (shouldDropOutputBuffer(j8, j2, z2)) {
                if (z5) {
                    skipOutputBuffer(mediaCodecAdapter, i, j5);
                } else {
                    dropOutputBuffer(mediaCodecAdapter, i, j5);
                }
                updateVideoFrameProcessingOffsetCounters(j8);
                return true;
            } else if (Util.SDK_INT >= 21) {
                if (j8 < 50000) {
                    notifyFrameMetadataListener(j5, adjustReleaseTime, format);
                    renderOutputBufferV21(mediaCodecAdapter, i, j5, adjustReleaseTime);
                    updateVideoFrameProcessingOffsetCounters(j8);
                    return true;
                }
            } else if (j8 < 30000) {
                if (j8 > 11000) {
                    try {
                        Thread.sleep((j8 - 10000) / 1000);
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
                notifyFrameMetadataListener(j5, adjustReleaseTime, format);
                renderOutputBuffer(mediaCodecAdapter, i, j5);
                updateVideoFrameProcessingOffsetCounters(j8);
                return true;
            }
        }
        return false;
    }

    private void notifyFrameMetadataListener(long j, long j2, Format format) {
        VideoFrameMetadataListener videoFrameMetadataListener = this.frameMetadataListener;
        if (videoFrameMetadataListener != null) {
            videoFrameMetadataListener.onVideoFrameAboutToBeRendered(j, j2, format, getCodecOutputMediaFormat());
        }
    }

    protected void onProcessedTunneledBuffer(long j) throws ExoPlaybackException {
        updateOutputFormatForTime(j);
        maybeNotifyVideoSizeChanged();
        this.decoderCounters.renderedOutputBufferCount++;
        maybeNotifyRenderedFirstFrame();
        onProcessedOutputBuffer(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProcessedTunneledEndOfStream() {
        setPendingOutputEndOfStream();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void onProcessedOutputBuffer(long j) {
        super.onProcessedOutputBuffer(j);
        if (this.tunneling) {
            return;
        }
        this.buffersInCodecCount--;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    public void onProcessedStreamChange() {
        super.onProcessedStreamChange();
        clearRenderedFirstFrame();
    }

    protected boolean shouldDropOutputBuffer(long j, long j2, boolean z) {
        return isBufferLate(j) && !z;
    }

    protected boolean shouldDropBuffersToKeyframe(long j, long j2, boolean z) {
        return isBufferVeryLate(j) && !z;
    }

    protected boolean shouldForceRenderOutputBuffer(long j, long j2) {
        return isBufferLate(j) && j2 > 100000;
    }

    protected void skipOutputBuffer(MediaCodecAdapter mediaCodecAdapter, int i, long j) {
        TraceUtil.beginSection("skipVideoBuffer");
        mediaCodecAdapter.releaseOutputBuffer(i, false);
        TraceUtil.endSection();
        this.decoderCounters.skippedOutputBufferCount++;
    }

    protected void dropOutputBuffer(MediaCodecAdapter mediaCodecAdapter, int i, long j) {
        TraceUtil.beginSection("dropVideoBuffer");
        mediaCodecAdapter.releaseOutputBuffer(i, false);
        TraceUtil.endSection();
        updateDroppedBufferCounters(0, 1);
    }

    protected boolean maybeDropBuffersToKeyframe(long j, boolean z) throws ExoPlaybackException {
        int skipSource = skipSource(j);
        if (skipSource == 0) {
            return false;
        }
        if (z) {
            DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.skippedInputBufferCount += skipSource;
            decoderCounters.skippedOutputBufferCount += this.buffersInCodecCount;
        } else {
            this.decoderCounters.droppedToKeyframeCount++;
            updateDroppedBufferCounters(skipSource, this.buffersInCodecCount);
        }
        flushOrReinitializeCodec();
        return true;
    }

    protected void updateDroppedBufferCounters(int i, int i2) {
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedInputBufferCount += i;
        int i3 = i + i2;
        decoderCounters.droppedBufferCount += i3;
        this.droppedFrames += i3;
        int i4 = this.consecutiveDroppedFrameCount + i3;
        this.consecutiveDroppedFrameCount = i4;
        decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(i4, decoderCounters.maxConsecutiveDroppedBufferCount);
        int i5 = this.maxDroppedFramesToNotify;
        if (i5 <= 0 || this.droppedFrames < i5) {
            return;
        }
        maybeNotifyDroppedFrames();
    }

    protected void updateVideoFrameProcessingOffsetCounters(long j) {
        this.decoderCounters.addVideoFrameProcessingOffset(j);
        this.totalVideoFrameProcessingOffsetUs += j;
        this.videoFrameProcessingOffsetCount++;
    }

    protected void renderOutputBuffer(MediaCodecAdapter mediaCodecAdapter, int i, long j) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodecAdapter.releaseOutputBuffer(i, true);
        TraceUtil.endSection();
        this.lastRenderRealtimeUs = SystemClock.elapsedRealtime() * 1000;
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    protected void renderOutputBufferV21(MediaCodecAdapter mediaCodecAdapter, int i, long j, long j2) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodecAdapter.releaseOutputBuffer(i, j2);
        TraceUtil.endSection();
        this.lastRenderRealtimeUs = SystemClock.elapsedRealtime() * 1000;
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    private boolean shouldUsePlaceholderSurface(MediaCodecInfo mediaCodecInfo) {
        return Util.SDK_INT >= 23 && !this.tunneling && !codecNeedsSetOutputSurfaceWorkaround(mediaCodecInfo.name) && (!mediaCodecInfo.secure || PlaceholderSurface.isSecureSupported(this.context));
    }

    private void releasePlaceholderSurface() {
        Surface surface = this.surface;
        PlaceholderSurface placeholderSurface = this.placeholderSurface;
        if (surface == placeholderSurface) {
            this.surface = null;
        }
        placeholderSurface.release();
        this.placeholderSurface = null;
    }

    private void setJoiningDeadlineMs() {
        this.joiningDeadlineMs = this.allowedJoiningTimeMs > 0 ? SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs : -9223372036854775807L;
    }

    private void clearRenderedFirstFrame() {
        MediaCodecAdapter codec;
        this.renderedFirstFrameAfterReset = false;
        if (Util.SDK_INT < 23 || !this.tunneling || (codec = getCodec()) == null) {
            return;
        }
        this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
    }

    void maybeNotifyRenderedFirstFrame() {
        this.renderedFirstFrameAfterEnable = true;
        if (this.renderedFirstFrameAfterReset) {
            return;
        }
        this.renderedFirstFrameAfterReset = true;
        this.eventDispatcher.renderedFirstFrame(this.surface);
        this.haveReportedFirstFrameRenderedForCurrentSurface = true;
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.haveReportedFirstFrameRenderedForCurrentSurface) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void clearReportedVideoSize() {
        this.reportedVideoSize = null;
    }

    private void maybeNotifyVideoSizeChanged() {
        int i = this.currentWidth;
        if (i == -1 && this.currentHeight == -1) {
            return;
        }
        VideoSize videoSize = this.reportedVideoSize;
        if (videoSize != null && videoSize.width == i && videoSize.height == this.currentHeight && videoSize.unappliedRotationDegrees == this.currentUnappliedRotationDegrees && videoSize.pixelWidthHeightRatio == this.currentPixelWidthHeightRatio) {
            return;
        }
        VideoSize videoSize2 = new VideoSize(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
        this.reportedVideoSize = videoSize2;
        this.eventDispatcher.videoSizeChanged(videoSize2);
    }

    private void maybeRenotifyVideoSizeChanged() {
        VideoSize videoSize = this.reportedVideoSize;
        if (videoSize != null) {
            this.eventDispatcher.videoSizeChanged(videoSize);
        }
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.eventDispatcher.droppedFrames(this.droppedFrames, elapsedRealtime - this.droppedFrameAccumulationStartTimeMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = elapsedRealtime;
        }
    }

    private void maybeNotifyVideoFrameProcessingOffset() {
        int i = this.videoFrameProcessingOffsetCount;
        if (i != 0) {
            this.eventDispatcher.reportVideoFrameProcessingOffset(this.totalVideoFrameProcessingOffsetUs, i);
            this.totalVideoFrameProcessingOffsetUs = 0L;
            this.videoFrameProcessingOffsetCount = 0;
        }
    }

    private static void setHdr10PlusInfoV29(MediaCodecAdapter mediaCodecAdapter, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("hdr10-plus-info", bArr);
        mediaCodecAdapter.setParameters(bundle);
    }

    protected void setOutputSurfaceV23(MediaCodecAdapter mediaCodecAdapter, Surface surface) {
        mediaCodecAdapter.setOutputSurface(surface);
    }

    private static void configureTunnelingV21(MediaFormat mediaFormat, int i) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", i);
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(21)
    protected MediaFormat getMediaFormat(Format format, String str, CodecMaxValues codecMaxValues, float f, boolean z, int i) {
        Pair<Integer, Integer> codecProfileAndLevel;
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", str);
        mediaFormat.setInteger("width", format.width);
        mediaFormat.setInteger("height", format.height);
        MediaFormatUtil.setCsdBuffers(mediaFormat, format.initializationData);
        MediaFormatUtil.maybeSetFloat(mediaFormat, "frame-rate", format.frameRate);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "rotation-degrees", format.rotationDegrees);
        MediaFormatUtil.maybeSetColorInfo(mediaFormat, format.colorInfo);
        if ("video/dolby-vision".equals(format.sampleMimeType) && (codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format)) != null) {
            MediaFormatUtil.maybeSetInteger(mediaFormat, "profile", ((Integer) codecProfileAndLevel.first).intValue());
        }
        mediaFormat.setInteger("max-width", codecMaxValues.width);
        mediaFormat.setInteger("max-height", codecMaxValues.height);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "max-input-size", codecMaxValues.inputSize);
        if (Util.SDK_INT >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (f != -1.0f) {
                mediaFormat.setFloat("operating-rate", f);
            }
        }
        if (z) {
            mediaFormat.setInteger("no-post-process", 1);
            mediaFormat.setInteger("auto-frc", 0);
        }
        if (i != 0) {
            configureTunnelingV21(mediaFormat, i);
        }
        return mediaFormat;
    }

    protected CodecMaxValues getCodecMaxValues(MediaCodecInfo mediaCodecInfo, Format format, Format[] formatArr) {
        int codecMaxInputSize;
        int i = format.width;
        int i2 = format.height;
        int maxInputSize = getMaxInputSize(mediaCodecInfo, format);
        if (formatArr.length == 1) {
            if (maxInputSize != -1 && (codecMaxInputSize = getCodecMaxInputSize(mediaCodecInfo, format)) != -1) {
                maxInputSize = Math.min((int) (maxInputSize * 1.5f), codecMaxInputSize);
            }
            return new CodecMaxValues(i, i2, maxInputSize);
        }
        int length = formatArr.length;
        boolean z = false;
        for (int i3 = 0; i3 < length; i3++) {
            Format format2 = formatArr[i3];
            if (format.colorInfo != null && format2.colorInfo == null) {
                format2 = format2.buildUpon().setColorInfo(format.colorInfo).build();
            }
            if (mediaCodecInfo.canReuseCodec(format, format2).result != 0) {
                int i4 = format2.width;
                z |= i4 == -1 || format2.height == -1;
                i = Math.max(i, i4);
                i2 = Math.max(i2, format2.height);
                maxInputSize = Math.max(maxInputSize, getMaxInputSize(mediaCodecInfo, format2));
            }
        }
        if (z) {
            Log.w("MediaCodecVideoRenderer", "Resolutions unknown. Codec max resolution: " + i + "x" + i2);
            Point codecMaxSize = getCodecMaxSize(mediaCodecInfo, format);
            if (codecMaxSize != null) {
                i = Math.max(i, codecMaxSize.x);
                i2 = Math.max(i2, codecMaxSize.y);
                maxInputSize = Math.max(maxInputSize, getCodecMaxInputSize(mediaCodecInfo, format.buildUpon().setWidth(i).setHeight(i2).build()));
                Log.w("MediaCodecVideoRenderer", "Codec max resolution adjusted to: " + i + "x" + i2);
            }
        }
        return new CodecMaxValues(i, i2, maxInputSize);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected MediaCodecDecoderException createDecoderException(Throwable th, MediaCodecInfo mediaCodecInfo) {
        return new MediaCodecVideoDecoderException(th, mediaCodecInfo, this.surface);
    }

    private static Point getCodecMaxSize(MediaCodecInfo mediaCodecInfo, Format format) {
        int[] iArr;
        int i = format.height;
        int i2 = format.width;
        boolean z = i > i2;
        int i3 = z ? i : i2;
        if (z) {
            i = i2;
        }
        float f = i / i3;
        for (int i4 : STANDARD_LONG_EDGE_VIDEO_PX) {
            int i5 = (int) (i4 * f);
            if (i4 <= i3 || i5 <= i) {
                break;
            }
            if (Util.SDK_INT >= 21) {
                int i6 = z ? i5 : i4;
                if (!z) {
                    i4 = i5;
                }
                Point alignVideoSizeV21 = mediaCodecInfo.alignVideoSizeV21(i6, i4);
                if (mediaCodecInfo.isVideoSizeAndRateSupportedV21(alignVideoSizeV21.x, alignVideoSizeV21.y, format.frameRate)) {
                    return alignVideoSizeV21;
                }
            } else {
                try {
                    int ceilDivide = Util.ceilDivide(i4, 16) * 16;
                    int ceilDivide2 = Util.ceilDivide(i5, 16) * 16;
                    if (ceilDivide * ceilDivide2 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                        int i7 = z ? ceilDivide2 : ceilDivide;
                        if (!z) {
                            ceilDivide = ceilDivide2;
                        }
                        return new Point(i7, ceilDivide);
                    }
                } catch (MediaCodecUtil.DecoderQueryException unused) {
                }
            }
        }
        return null;
    }

    protected static int getMaxInputSize(MediaCodecInfo mediaCodecInfo, Format format) {
        if (format.maxInputSize != -1) {
            int size = format.initializationData.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                i += format.initializationData.get(i2).length;
            }
            return format.maxInputSize + i;
        }
        return getCodecMaxInputSize(mediaCodecInfo, format);
    }

    private static boolean deviceNeedsNoPostProcessWorkaround() {
        return "NVIDIA".equals(Util.MANUFACTURER);
    }

    protected boolean codecNeedsSetOutputSurfaceWorkaround(String str) {
        if (str.startsWith("OMX.google")) {
            return false;
        }
        synchronized (MediaCodecVideoRenderer.class) {
            if (!evaluatedDeviceNeedsSetOutputSurfaceWorkaround) {
                deviceNeedsSetOutputSurfaceWorkaround = evaluateDeviceNeedsSetOutputSurfaceWorkaround();
                evaluatedDeviceNeedsSetOutputSurfaceWorkaround = true;
            }
        }
        return deviceNeedsSetOutputSurfaceWorkaround;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int i, int i2, int i3) {
            this.width = i;
            this.height = i2;
            this.inputSize = i3;
        }
    }

    private static int getMaxSampleSize(int i, int i2) {
        return (i * 3) / (i2 * 2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0729, code lost:
        if (r0.equals("ELUGA_Ray_X") == false) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean evaluateDeviceNeedsSetOutputSurfaceWorkaround() {
        char c;
        char c2;
        int i = Util.SDK_INT;
        char c3 = 28;
        if (i <= 28) {
            String str = Util.DEVICE;
            str.hashCode();
            switch (str.hashCode()) {
                case -1339091551:
                    if (str.equals("dangal")) {
                        c2 = 0;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -1220081023:
                    if (str.equals("dangalFHD")) {
                        c2 = 1;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -1220066608:
                    if (str.equals("dangalUHD")) {
                        c2 = 2;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -1012436106:
                    if (str.equals("oneday")) {
                        c2 = 3;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -760312546:
                    if (str.equals("aquaman")) {
                        c2 = 4;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -64886864:
                    if (str.equals("magnolia")) {
                        c2 = 5;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 3415681:
                    if (str.equals("once")) {
                        c2 = 6;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 825323514:
                    if (str.equals("machuca")) {
                        c2 = 7;
                        break;
                    }
                    c2 = 65535;
                    break;
                default:
                    c2 = 65535;
                    break;
            }
            switch (c2) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    return true;
            }
        }
        if (i > 27 || !"HWEML".equals(Util.DEVICE)) {
            String str2 = Util.MODEL;
            str2.hashCode();
            switch (str2.hashCode()) {
                case -349662828:
                    if (str2.equals("AFTJMST12")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -321033677:
                    if (str2.equals("AFTKMST12")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 2006354:
                    if (str2.equals("AFTA")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 2006367:
                    if (str2.equals("AFTN")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 2006371:
                    if (str2.equals("AFTR")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1785421873:
                    if (str2.equals("AFTEU011")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1785421876:
                    if (str2.equals("AFTEU014")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 1798172390:
                    if (str2.equals("AFTSO001")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 2119412532:
                    if (str2.equals("AFTEUFF014")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case '\b':
                    return true;
                default:
                    if (i <= 26) {
                        String str3 = Util.DEVICE;
                        str3.hashCode();
                        switch (str3.hashCode()) {
                            case -2144781245:
                                if (str3.equals("GIONEE_SWW1609")) {
                                    c3 = 0;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -2144781185:
                                if (str3.equals("GIONEE_SWW1627")) {
                                    c3 = 1;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -2144781160:
                                if (str3.equals("GIONEE_SWW1631")) {
                                    c3 = 2;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -2097309513:
                                if (str3.equals("K50a40")) {
                                    c3 = 3;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -2022874474:
                                if (str3.equals("CP8676_I02")) {
                                    c3 = 4;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1978993182:
                                if (str3.equals("NX541J")) {
                                    c3 = 5;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1978990237:
                                if (str3.equals("NX573J")) {
                                    c3 = 6;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1936688988:
                                if (str3.equals("PGN528")) {
                                    c3 = 7;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1936688066:
                                if (str3.equals("PGN610")) {
                                    c3 = '\b';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1936688065:
                                if (str3.equals("PGN611")) {
                                    c3 = '\t';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1931988508:
                                if (str3.equals("AquaPowerM")) {
                                    c3 = '\n';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1885099851:
                                if (str3.equals("RAIJIN")) {
                                    c3 = 11;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1696512866:
                                if (str3.equals("XT1663")) {
                                    c3 = '\f';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1680025915:
                                if (str3.equals("ComioS1")) {
                                    c3 = '\r';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1615810839:
                                if (str3.equals("Phantom6")) {
                                    c3 = 14;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1600724499:
                                if (str3.equals("pacificrim")) {
                                    c3 = 15;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1554255044:
                                if (str3.equals("vernee_M5")) {
                                    c3 = 16;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1481772737:
                                if (str3.equals("panell_dl")) {
                                    c3 = 17;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1481772730:
                                if (str3.equals("panell_ds")) {
                                    c3 = 18;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1481772729:
                                if (str3.equals("panell_dt")) {
                                    c3 = 19;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1320080169:
                                if (str3.equals("GiONEE_GBL7319")) {
                                    c3 = 20;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1217592143:
                                if (str3.equals("BRAVIA_ATV2")) {
                                    c3 = 21;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1180384755:
                                if (str3.equals("iris60")) {
                                    c3 = 22;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1139198265:
                                if (str3.equals("Slate_Pro")) {
                                    c3 = 23;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -1052835013:
                                if (str3.equals("namath")) {
                                    c3 = 24;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -993250464:
                                if (str3.equals("A10-70F")) {
                                    c3 = 25;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -993250458:
                                if (str3.equals("A10-70L")) {
                                    c3 = 26;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -965403638:
                                if (str3.equals("s905x018")) {
                                    c3 = 27;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -958336948:
                                break;
                            case -879245230:
                                if (str3.equals("tcl_eu")) {
                                    c3 = 29;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -842500323:
                                if (str3.equals("nicklaus_f")) {
                                    c3 = 30;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -821392978:
                                if (str3.equals("A7000-a")) {
                                    c3 = 31;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -797483286:
                                if (str3.equals("SVP-DTV15")) {
                                    c3 = ' ';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -794946968:
                                if (str3.equals("watson")) {
                                    c3 = '!';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -788334647:
                                if (str3.equals("whyred")) {
                                    c3 = '\"';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -782144577:
                                if (str3.equals("OnePlus5T")) {
                                    c3 = '#';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -575125681:
                                if (str3.equals("GiONEE_CBL7513")) {
                                    c3 = '$';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -521118391:
                                if (str3.equals("GIONEE_GBL7360")) {
                                    c3 = '%';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -430914369:
                                if (str3.equals("Pixi4-7_3G")) {
                                    c3 = '&';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -290434366:
                                if (str3.equals("taido_row")) {
                                    c3 = '\'';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -282781963:
                                if (str3.equals("BLACK-1X")) {
                                    c3 = '(';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -277133239:
                                if (str3.equals("Z12_PRO")) {
                                    c3 = ')';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -173639913:
                                if (str3.equals("ELUGA_A3_Pro")) {
                                    c3 = '*';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case -56598463:
                                if (str3.equals("woods_fn")) {
                                    c3 = '+';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2126:
                                if (str3.equals("C1")) {
                                    c3 = ',';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2564:
                                if (str3.equals("Q5")) {
                                    c3 = '-';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2715:
                                if (str3.equals("V1")) {
                                    c3 = '.';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2719:
                                if (str3.equals("V5")) {
                                    c3 = '/';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3091:
                                if (str3.equals("b5")) {
                                    c3 = '0';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3483:
                                if (str3.equals("mh")) {
                                    c3 = '1';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 73405:
                                if (str3.equals("JGZ")) {
                                    c3 = '2';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 75537:
                                if (str3.equals("M04")) {
                                    c3 = '3';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 75739:
                                if (str3.equals("M5c")) {
                                    c3 = '4';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 76779:
                                if (str3.equals("MX6")) {
                                    c3 = '5';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 78669:
                                if (str3.equals("P85")) {
                                    c3 = '6';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 79305:
                                if (str3.equals("PLE")) {
                                    c3 = '7';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 80618:
                                if (str3.equals("QX1")) {
                                    c3 = '8';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 88274:
                                if (str3.equals("Z80")) {
                                    c3 = '9';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 98846:
                                if (str3.equals("cv1")) {
                                    c3 = ':';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 98848:
                                if (str3.equals("cv3")) {
                                    c3 = ';';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 99329:
                                if (str3.equals("deb")) {
                                    c3 = '<';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 101481:
                                if (str3.equals("flo")) {
                                    c3 = '=';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1513190:
                                if (str3.equals("1601")) {
                                    c3 = '>';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1514184:
                                if (str3.equals("1713")) {
                                    c3 = '?';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1514185:
                                if (str3.equals("1714")) {
                                    c3 = '@';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133089:
                                if (str3.equals("F01H")) {
                                    c3 = 'A';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133091:
                                if (str3.equals("F01J")) {
                                    c3 = 'B';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133120:
                                if (str3.equals("F02H")) {
                                    c3 = 'C';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133151:
                                if (str3.equals("F03H")) {
                                    c3 = 'D';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133182:
                                if (str3.equals("F04H")) {
                                    c3 = 'E';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2133184:
                                if (str3.equals("F04J")) {
                                    c3 = 'F';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2436959:
                                if (str3.equals("P681")) {
                                    c3 = 'G';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2463773:
                                if (str3.equals("Q350")) {
                                    c3 = 'H';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2464648:
                                if (str3.equals("Q427")) {
                                    c3 = 'I';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2689555:
                                if (str3.equals("XE2X")) {
                                    c3 = 'J';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3154429:
                                if (str3.equals("fugu")) {
                                    c3 = 'K';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3284551:
                                if (str3.equals("kate")) {
                                    c3 = 'L';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3351335:
                                if (str3.equals("mido")) {
                                    c3 = 'M';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 3386211:
                                if (str3.equals("p212")) {
                                    c3 = 'N';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 41325051:
                                if (str3.equals("MEIZU_M5")) {
                                    c3 = 'O';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 51349633:
                                if (str3.equals("601LV")) {
                                    c3 = 'P';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 51350594:
                                if (str3.equals("602LV")) {
                                    c3 = 'Q';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 55178625:
                                if (str3.equals("Aura_Note_2")) {
                                    c3 = 'R';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 61542055:
                                if (str3.equals("A1601")) {
                                    c3 = 'S';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 65355429:
                                if (str3.equals("E5643")) {
                                    c3 = 'T';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66214468:
                                if (str3.equals("F3111")) {
                                    c3 = 'U';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66214470:
                                if (str3.equals("F3113")) {
                                    c3 = 'V';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66214473:
                                if (str3.equals("F3116")) {
                                    c3 = 'W';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66215429:
                                if (str3.equals("F3211")) {
                                    c3 = 'X';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66215431:
                                if (str3.equals("F3213")) {
                                    c3 = 'Y';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66215433:
                                if (str3.equals("F3215")) {
                                    c3 = 'Z';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 66216390:
                                if (str3.equals("F3311")) {
                                    c3 = '[';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 76402249:
                                if (str3.equals("PRO7S")) {
                                    c3 = '\\';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 76404105:
                                if (str3.equals("Q4260")) {
                                    c3 = ']';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 76404911:
                                if (str3.equals("Q4310")) {
                                    c3 = '^';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 80963634:
                                if (str3.equals("V23GB")) {
                                    c3 = '_';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 82882791:
                                if (str3.equals("X3_HK")) {
                                    c3 = '`';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 98715550:
                                if (str3.equals("i9031")) {
                                    c3 = 'a';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 101370885:
                                if (str3.equals("l5460")) {
                                    c3 = 'b';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 102844228:
                                if (str3.equals("le_x6")) {
                                    c3 = 'c';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 165221241:
                                if (str3.equals("A2016a40")) {
                                    c3 = 'd';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 182191441:
                                if (str3.equals("CPY83_I00")) {
                                    c3 = 'e';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 245388979:
                                if (str3.equals("marino_f")) {
                                    c3 = 'f';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 287431619:
                                if (str3.equals("griffin")) {
                                    c3 = 'g';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 307593612:
                                if (str3.equals("A7010a48")) {
                                    c3 = 'h';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 308517133:
                                if (str3.equals("A7020a48")) {
                                    c3 = 'i';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 316215098:
                                if (str3.equals("TB3-730F")) {
                                    c3 = 'j';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 316215116:
                                if (str3.equals("TB3-730X")) {
                                    c3 = 'k';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 316246811:
                                if (str3.equals("TB3-850F")) {
                                    c3 = 'l';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 316246818:
                                if (str3.equals("TB3-850M")) {
                                    c3 = 'm';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 407160593:
                                if (str3.equals("Pixi5-10_4G")) {
                                    c3 = 'n';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 507412548:
                                if (str3.equals("QM16XE_U")) {
                                    c3 = 'o';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 793982701:
                                if (str3.equals("GIONEE_WBL5708")) {
                                    c3 = 'p';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 794038622:
                                if (str3.equals("GIONEE_WBL7365")) {
                                    c3 = 'q';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 794040393:
                                if (str3.equals("GIONEE_WBL7519")) {
                                    c3 = 'r';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 835649806:
                                if (str3.equals("manning")) {
                                    c3 = 's';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 917340916:
                                if (str3.equals("A7000plus")) {
                                    c3 = 't';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 958008161:
                                if (str3.equals("j2xlteins")) {
                                    c3 = 'u';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1060579533:
                                if (str3.equals("panell_d")) {
                                    c3 = 'v';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1150207623:
                                if (str3.equals("LS-5017")) {
                                    c3 = 'w';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1176899427:
                                if (str3.equals("itel_S41")) {
                                    c3 = 'x';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1280332038:
                                if (str3.equals("hwALE-H")) {
                                    c3 = 'y';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1306947716:
                                if (str3.equals("EverStar_S")) {
                                    c3 = 'z';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1349174697:
                                if (str3.equals("htc_e56ml_dtul")) {
                                    c3 = '{';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1522194893:
                                if (str3.equals("woods_f")) {
                                    c3 = '|';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1691543273:
                                if (str3.equals("CPH1609")) {
                                    c3 = '}';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1691544261:
                                if (str3.equals("CPH1715")) {
                                    c3 = '~';
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1709443163:
                                if (str3.equals("iball8735_9806")) {
                                    c3 = 127;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1865889110:
                                if (str3.equals("santoni")) {
                                    c3 = 128;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1906253259:
                                if (str3.equals("PB2-670M")) {
                                    c3 = 129;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 1977196784:
                                if (str3.equals("Infinix-X572")) {
                                    c3 = 130;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2006372676:
                                if (str3.equals("BRAVIA_ATV3_4K")) {
                                    c3 = 131;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2019281702:
                                if (str3.equals("DM-01K")) {
                                    c3 = 132;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2029784656:
                                if (str3.equals("HWBLN-H")) {
                                    c3 = 133;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2030379515:
                                if (str3.equals("HWCAM-H")) {
                                    c3 = 134;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2033393791:
                                if (str3.equals("ASUS_X00AD_2")) {
                                    c3 = 135;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2047190025:
                                if (str3.equals("ELUGA_Note")) {
                                    c3 = 136;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2047252157:
                                if (str3.equals("ELUGA_Prim")) {
                                    c3 = 137;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2048319463:
                                if (str3.equals("HWVNS-H")) {
                                    c3 = 138;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            case 2048855701:
                                if (str3.equals("HWWAS-H")) {
                                    c3 = 139;
                                    break;
                                }
                                c3 = 65535;
                                break;
                            default:
                                c3 = 65535;
                                break;
                        }
                        switch (c3) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case '\b':
                            case '\t':
                            case '\n':
                            case 11:
                            case '\f':
                            case '\r':
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                            case 31:
                            case ' ':
                            case '!':
                            case '\"':
                            case '#':
                            case '$':
                            case '%':
                            case '&':
                            case '\'':
                            case '(':
                            case ')':
                            case '*':
                            case '+':
                            case ',':
                            case '-':
                            case '.':
                            case '/':
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                            case ':':
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                            case '?':
                            case '@':
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'S':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            case 'Z':
                            case '[':
                            case '\\':
                            case ']':
                            case '^':
                            case '_':
                            case '`':
                            case 'a':
                            case 'b':
                            case 'c':
                            case FileLoader.MEDIA_DIR_IMAGE_PUBLIC /* 100 */:
                            case FileLoader.MEDIA_DIR_VIDEO_PUBLIC /* 101 */:
                            case 'f':
                            case 'g':
                            case 'h':
                            case 'i':
                            case 'j':
                            case 'k':
                            case 'l':
                            case 'm':
                            case 'n':
                            case 'o':
                            case 'p':
                            case 'q':
                            case 'r':
                            case 's':
                            case 't':
                            case MessagesStorage.LAST_DB_VERSION /* 117 */:
                            case 'v':
                            case 'w':
                            case 'x':
                            case 'y':
                            case 'z':
                            case '{':
                            case '|':
                            case '}':
                            case '~':
                            case 127:
                            case 128:
                            case 129:
                            case 130:
                            case 131:
                            case 132:
                            case 133:
                            case 134:
                            case 135:
                            case 136:
                            case 137:
                            case 138:
                            case 139:
                                break;
                            default:
                                str2.hashCode();
                                if (!str2.equals("JSN-L21")) {
                                }
                                break;
                        }
                        return true;
                    }
                    return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class OnFrameRenderedListenerV23 implements MediaCodecAdapter.OnFrameRenderedListener, Handler.Callback {
        private final Handler handler;

        public OnFrameRenderedListenerV23(MediaCodecAdapter mediaCodecAdapter) {
            Handler createHandlerForCurrentLooper = Util.createHandlerForCurrentLooper(this);
            this.handler = createHandlerForCurrentLooper;
            mediaCodecAdapter.setOnFrameRenderedListener(this, createHandlerForCurrentLooper);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter.OnFrameRenderedListener
        public void onFrameRendered(MediaCodecAdapter mediaCodecAdapter, long j, long j2) {
            if (Util.SDK_INT < 30) {
                this.handler.sendMessageAtFrontOfQueue(Message.obtain(this.handler, 0, (int) (j >> 32), (int) j));
                return;
            }
            handleFrameRendered(j);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            handleFrameRendered(Util.toLong(message.arg1, message.arg2));
            return true;
        }

        private void handleFrameRendered(long j) {
            MediaCodecVideoRenderer mediaCodecVideoRenderer = MediaCodecVideoRenderer.this;
            if (this != mediaCodecVideoRenderer.tunnelingOnFrameRenderedListener || mediaCodecVideoRenderer.getCodec() == null) {
                return;
            }
            if (j == Long.MAX_VALUE) {
                MediaCodecVideoRenderer.this.onProcessedTunneledEndOfStream();
                return;
            }
            try {
                MediaCodecVideoRenderer.this.onProcessedTunneledBuffer(j);
            } catch (ExoPlaybackException e) {
                MediaCodecVideoRenderer.this.setPendingPlaybackException(e);
            }
        }
    }
}
