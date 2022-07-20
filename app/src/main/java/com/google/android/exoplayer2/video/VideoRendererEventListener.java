package com.google.android.exoplayer2.video;

import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public interface VideoRendererEventListener {
    void onDroppedFrames(int i, long j);

    void onRenderedFirstFrame(Surface surface);

    void onVideoDecoderInitialized(String str, long j, long j2);

    void onVideoDisabled(DecoderCounters decoderCounters);

    void onVideoEnabled(DecoderCounters decoderCounters);

    void onVideoInputFormatChanged(Format format);

    void onVideoSizeChanged(int i, int i2, int i3, float f);

    /* loaded from: classes.dex */
    public static final class EventDispatcher {
        private final Handler handler;
        private final VideoRendererEventListener listener;

        public EventDispatcher(Handler handler, VideoRendererEventListener videoRendererEventListener) {
            this.handler = videoRendererEventListener != null ? (Handler) Assertions.checkNotNull(handler) : null;
            this.listener = videoRendererEventListener;
        }

        public void enabled(DecoderCounters decoderCounters) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda4(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$enabled$0(DecoderCounters decoderCounters) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoEnabled(decoderCounters);
        }

        public void decoderInitialized(String str, long j, long j2) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda6(this, str, j, j2));
            }
        }

        public /* synthetic */ void lambda$decoderInitialized$1(String str, long j, long j2) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoDecoderInitialized(str, j, j2);
        }

        public void inputFormatChanged(Format format) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda3(this, format));
            }
        }

        public /* synthetic */ void lambda$inputFormatChanged$2(Format format) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoInputFormatChanged(format);
        }

        public void droppedFrames(int i, long j) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda1(this, i, j));
            }
        }

        public /* synthetic */ void lambda$droppedFrames$3(int i, long j) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onDroppedFrames(i, j);
        }

        public void videoSizeChanged(int i, int i2, int i3, float f) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda0(this, i, i2, i3, f));
            }
        }

        public /* synthetic */ void lambda$videoSizeChanged$4(int i, int i2, int i3, float f) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoSizeChanged(i, i2, i3, f);
        }

        public void renderedFirstFrame(Surface surface) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2(this, surface));
            }
        }

        public /* synthetic */ void lambda$renderedFirstFrame$5(Surface surface) {
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onRenderedFirstFrame(surface);
        }

        public void disabled(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda5(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$disabled$6(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            ((VideoRendererEventListener) Util.castNonNull(this.listener)).onVideoDisabled(decoderCounters);
        }
    }
}
