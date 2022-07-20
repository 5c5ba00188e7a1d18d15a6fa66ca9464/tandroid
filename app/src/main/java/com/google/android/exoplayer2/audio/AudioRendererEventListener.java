package com.google.android.exoplayer2.audio;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public interface AudioRendererEventListener {
    void onAudioDecoderInitialized(String str, long j, long j2);

    void onAudioDisabled(DecoderCounters decoderCounters);

    void onAudioEnabled(DecoderCounters decoderCounters);

    void onAudioInputFormatChanged(Format format);

    void onAudioSessionId(int i);

    void onAudioSinkUnderrun(int i, long j, long j2);

    /* loaded from: classes.dex */
    public static final class EventDispatcher {
        private final Handler handler;
        private final AudioRendererEventListener listener;

        public EventDispatcher(Handler handler, AudioRendererEventListener audioRendererEventListener) {
            this.handler = audioRendererEventListener != null ? (Handler) Assertions.checkNotNull(handler) : null;
            this.listener = audioRendererEventListener;
        }

        public void enabled(DecoderCounters decoderCounters) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda3(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$enabled$0(DecoderCounters decoderCounters) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioEnabled(decoderCounters);
        }

        public void decoderInitialized(String str, long j, long j2) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda5(this, str, j, j2));
            }
        }

        public /* synthetic */ void lambda$decoderInitialized$1(String str, long j, long j2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioDecoderInitialized(str, j, j2);
        }

        public void inputFormatChanged(Format format) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2(this, format));
            }
        }

        public /* synthetic */ void lambda$inputFormatChanged$2(Format format) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioInputFormatChanged(format);
        }

        public void audioTrackUnderrun(int i, long j, long j2) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda1(this, i, j, j2));
            }
        }

        public /* synthetic */ void lambda$audioTrackUnderrun$3(int i, long j, long j2) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioSinkUnderrun(i, j, j2);
        }

        public void disabled(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda4(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$disabled$4(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioDisabled(decoderCounters);
        }

        public void audioSessionId(int i) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new AudioRendererEventListener$EventDispatcher$$ExternalSyntheticLambda0(this, i));
            }
        }

        public /* synthetic */ void lambda$audioSessionId$5(int i) {
            ((AudioRendererEventListener) Util.castNonNull(this.listener)).onAudioSessionId(i);
        }
    }
}
