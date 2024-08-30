package com.google.android.exoplayer2;

import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.MediaClock;
/* loaded from: classes.dex */
public interface Renderer extends PlayerMessage.Target {

    /* loaded from: classes.dex */
    public abstract /* synthetic */ class -CC {
        public static void $default$setPlaybackSpeed(Renderer renderer, float f, float f2) {
        }
    }

    /* loaded from: classes.dex */
    public interface WakeupListener {
        void onSleep();

        void onWakeup();
    }

    void disable();

    void enable(RendererConfiguration rendererConfiguration, Format[] formatArr, SampleStream sampleStream, long j, boolean z, boolean z2, long j2, long j3);

    RendererCapabilities getCapabilities();

    MediaClock getMediaClock();

    String getName();

    long getReadingPositionUs();

    int getState();

    SampleStream getStream();

    int getTrackType();

    boolean hasReadStreamToEnd();

    void init(int i, PlayerId playerId);

    boolean isCurrentStreamFinal();

    boolean isEnded();

    boolean isReady();

    void maybeThrowStreamError();

    void render(long j, long j2);

    void replaceStream(Format[] formatArr, SampleStream sampleStream, long j, long j2);

    void reset();

    void resetPosition(long j);

    void setCurrentStreamFinal();

    void setPlaybackSpeed(float f, float f2);

    void start();

    void stop();
}
