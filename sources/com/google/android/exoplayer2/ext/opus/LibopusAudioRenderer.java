package com.google.android.exoplayer2.ext.opus;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DecoderAudioRenderer;
import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public class LibopusAudioRenderer extends DecoderAudioRenderer {
    private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
    private static final int NUM_BUFFERS = 16;
    private static final String TAG = "LibopusAudioRenderer";

    public LibopusAudioRenderer() {
        this((Handler) null, (AudioRendererEventListener) null, new AudioProcessor[0]);
    }

    public LibopusAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioSink audioSink) {
        super(handler, audioRendererEventListener, audioSink);
    }

    public LibopusAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioProcessor... audioProcessorArr) {
        super(handler, audioRendererEventListener, audioProcessorArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    public final OpusDecoder createDecoder(Format format, CryptoConfig cryptoConfig) {
        TraceUtil.beginSection("createOpusDecoder");
        boolean z = getSinkFormatSupport(Util.getPcmFormat(4, format.channelCount, format.sampleRate)) == 2;
        int i = format.maxInputSize;
        OpusDecoder opusDecoder = new OpusDecoder(16, 16, i != -1 ? i : DEFAULT_INPUT_BUFFER_SIZE, format.initializationData, cryptoConfig, z);
        opusDecoder.experimentalSetDiscardPaddingEnabled(experimentalGetDiscardPaddingEnabled());
        TraceUtil.endSection();
        return opusDecoder;
    }

    protected boolean experimentalGetDiscardPaddingEnabled() {
        return false;
    }

    @Override // com.google.android.exoplayer2.Renderer, com.google.android.exoplayer2.RendererCapabilities
    public String getName() {
        return TAG;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    public final Format getOutputFormat(OpusDecoder opusDecoder) {
        return Util.getPcmFormat(opusDecoder.outputFloat ? 4 : 2, opusDecoder.channelCount, 48000);
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.Renderer
    public /* bridge */ /* synthetic */ void setPlaybackSpeed(float f, float f2) {
        Renderer.-CC.$default$setPlaybackSpeed(this, f, f2);
    }

    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    protected int supportsFormatInternal(Format format) {
        boolean supportsCryptoType = OpusLibrary.supportsCryptoType(format.cryptoType);
        if (!OpusLibrary.isAvailable() || !"audio/opus".equalsIgnoreCase(format.sampleMimeType)) {
            return 0;
        }
        if (sinkSupportsFormat(Util.getPcmFormat(2, format.channelCount, format.sampleRate))) {
            return !supportsCryptoType ? 2 : 4;
        }
        return 1;
    }
}
