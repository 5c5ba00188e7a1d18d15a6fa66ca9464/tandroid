package com.google.android.exoplayer2;

import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.MediaClock;
import java.io.IOException;
/* loaded from: classes.dex */
public interface Renderer extends PlayerMessage.Target {

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        public static void $default$setPlaybackSpeed(Renderer renderer, float f, float f2) throws ExoPlaybackException {
        }
    }

    /* loaded from: classes.dex */
    public interface WakeupListener {
        void onSleep();

        void onWakeup();
    }

    void disable();

    void enable(RendererConfiguration rendererConfiguration, Format[] formatArr, SampleStream sampleStream, long j, boolean z, boolean z2, long j2, long j3) throws ExoPlaybackException;

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

    void maybeThrowStreamError() throws IOException;

    void render(long j, long j2) throws ExoPlaybackException;

    void replaceStream(Format[] formatArr, SampleStream sampleStream, long j, long j2) throws ExoPlaybackException;

    void reset();

    void resetPosition(long j) throws ExoPlaybackException;

    void setCurrentStreamFinal();

    void setPlaybackSpeed(float f, float f2) throws ExoPlaybackException;

    void start() throws ExoPlaybackException;

    void stop();
}
