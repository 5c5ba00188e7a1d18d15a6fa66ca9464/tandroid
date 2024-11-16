package com.google.android.exoplayer2.upstream;

import android.os.Handler;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public interface BandwidthMeter {

    public abstract /* synthetic */ class -CC {
        public static long $default$getTimeToFirstByteEstimateUs(BandwidthMeter bandwidthMeter) {
            return -9223372036854775807L;
        }
    }

    public interface EventListener {

        public static final class EventDispatcher {
            private final CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();

            /* JADX INFO: Access modifiers changed from: private */
            static final class HandlerAndListener {
                private final Handler handler;
                private final EventListener listener;
                private boolean released;

                public HandlerAndListener(Handler handler, EventListener eventListener) {
                    this.handler = handler;
                    this.listener = eventListener;
                }

                public void release() {
                    this.released = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ void lambda$bandwidthSample$0(HandlerAndListener handlerAndListener, int i, long j, long j2) {
                handlerAndListener.listener.onBandwidthSample(i, j, j2);
            }

            public void addListener(Handler handler, EventListener eventListener) {
                Assertions.checkNotNull(handler);
                Assertions.checkNotNull(eventListener);
                removeListener(eventListener);
                this.listeners.add(new HandlerAndListener(handler, eventListener));
            }

            public void bandwidthSample(final int i, final long j, final long j2) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    final HandlerAndListener handlerAndListener = (HandlerAndListener) it.next();
                    if (!handlerAndListener.released) {
                        handlerAndListener.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.upstream.BandwidthMeter$EventListener$EventDispatcher$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                BandwidthMeter.EventListener.EventDispatcher.lambda$bandwidthSample$0(BandwidthMeter.EventListener.EventDispatcher.HandlerAndListener.this, i, j, j2);
                            }
                        });
                    }
                }
            }

            public void removeListener(EventListener eventListener) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    HandlerAndListener handlerAndListener = (HandlerAndListener) it.next();
                    if (handlerAndListener.listener == eventListener) {
                        handlerAndListener.release();
                        this.listeners.remove(handlerAndListener);
                    }
                }
            }
        }

        void onBandwidthSample(int i, long j, long j2);
    }

    void addEventListener(Handler handler, EventListener eventListener);

    long getBitrateEstimate();

    long getTimeToFirstByteEstimateUs();

    TransferListener getTransferListener();

    void removeEventListener(EventListener eventListener);
}
