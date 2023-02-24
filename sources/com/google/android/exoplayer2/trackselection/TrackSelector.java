package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
/* loaded from: classes.dex */
public abstract class TrackSelector {
    private BandwidthMeter bandwidthMeter;
    private InvalidationListener listener;

    /* loaded from: classes.dex */
    public interface InvalidationListener {
        void onTrackSelectionsInvalidated();
    }

    public boolean isSetParametersSupported() {
        return false;
    }

    public abstract void onSelectionActivated(Object obj);

    public abstract TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilitiesArr, TrackGroupArray trackGroupArray, MediaSource.MediaPeriodId mediaPeriodId, Timeline timeline) throws ExoPlaybackException;

    public void setAudioAttributes(AudioAttributes audioAttributes) {
    }

    public void init(InvalidationListener invalidationListener, BandwidthMeter bandwidthMeter) {
        this.listener = invalidationListener;
        this.bandwidthMeter = bandwidthMeter;
    }

    public void release() {
        this.listener = null;
        this.bandwidthMeter = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void invalidate() {
        InvalidationListener invalidationListener = this.listener;
        if (invalidationListener != null) {
            invalidationListener.onTrackSelectionsInvalidated();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final BandwidthMeter getBandwidthMeter() {
        return (BandwidthMeter) Assertions.checkStateNotNull(this.bandwidthMeter);
    }
}
