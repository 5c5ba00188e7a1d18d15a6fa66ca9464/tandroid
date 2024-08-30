package com.google.android.exoplayer2.ext.flac;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DecoderAudioRenderer;
import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.extractor.FlacStreamMetadata;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public final class LibflacAudioRenderer extends DecoderAudioRenderer {
    private static final int METADATA_BLOCK_HEADER_SIZE = 4;
    private static final int NUM_BUFFERS = 16;
    private static final int STREAM_MARKER_SIZE = 4;
    private static final String TAG = "LibflacAudioRenderer";

    public LibflacAudioRenderer() {
        this((Handler) null, (AudioRendererEventListener) null, new AudioProcessor[0]);
    }

    public LibflacAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioSink audioSink) {
        super(handler, audioRendererEventListener, audioSink);
    }

    public LibflacAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioProcessor... audioProcessorArr) {
        super(handler, audioRendererEventListener, audioProcessorArr);
    }

    private static Format getOutputFormat(FlacStreamMetadata flacStreamMetadata) {
        return Util.getPcmFormat(Util.getPcmEncoding(flacStreamMetadata.bitsPerSample), flacStreamMetadata.channels, flacStreamMetadata.sampleRate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    public FlacDecoder createDecoder(Format format, CryptoConfig cryptoConfig) {
        TraceUtil.beginSection("createFlacDecoder");
        FlacDecoder flacDecoder = new FlacDecoder(16, 16, format.maxInputSize, format.initializationData);
        TraceUtil.endSection();
        return flacDecoder;
    }

    @Override // com.google.android.exoplayer2.Renderer, com.google.android.exoplayer2.RendererCapabilities
    public String getName() {
        return TAG;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    public Format getOutputFormat(FlacDecoder flacDecoder) {
        return getOutputFormat(flacDecoder.getStreamMetadata());
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.Renderer
    public /* bridge */ /* synthetic */ void setPlaybackSpeed(float f, float f2) {
        Renderer.-CC.$default$setPlaybackSpeed(this, f, f2);
    }

    @Override // com.google.android.exoplayer2.audio.DecoderAudioRenderer
    protected int supportsFormatInternal(Format format) {
        if (FlacLibrary.isAvailable() && "audio/flac".equalsIgnoreCase(format.sampleMimeType)) {
            if (sinkSupportsFormat(format.initializationData.isEmpty() ? Util.getPcmFormat(2, format.channelCount, format.sampleRate) : getOutputFormat(new FlacStreamMetadata((byte[]) format.initializationData.get(0), 8)))) {
                return format.cryptoType != 0 ? 2 : 4;
            }
            return 1;
        }
        return 0;
    }
}
