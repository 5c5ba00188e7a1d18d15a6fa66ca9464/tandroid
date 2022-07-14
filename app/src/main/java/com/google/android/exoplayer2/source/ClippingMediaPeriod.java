package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
/* loaded from: classes3.dex */
public final class ClippingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private MediaPeriod.Callback callback;
    long endUs;
    public final MediaPeriod mediaPeriod;
    private long pendingInitialDiscontinuityPositionUs;
    private ClippingSampleStream[] sampleStreams = new ClippingSampleStream[0];
    long startUs;

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public /* synthetic */ List getStreamKeys(List list) {
        List emptyList;
        emptyList = Collections.emptyList();
        return emptyList;
    }

    public ClippingMediaPeriod(MediaPeriod mediaPeriod, boolean enableInitialDiscontinuity, long startUs, long endUs) {
        this.mediaPeriod = mediaPeriod;
        this.pendingInitialDiscontinuityPositionUs = enableInitialDiscontinuity ? startUs : C.TIME_UNSET;
        this.startUs = startUs;
        this.endUs = endUs;
    }

    public void updateClipping(long startUs, long endUs) {
        this.startUs = startUs;
        this.endUs = endUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback, long positionUs) {
        this.callback = callback;
        this.mediaPeriod.prepare(this, positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x005e, code lost:
        if (r1 > r3) goto L25;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x006b  */
    @Override // com.google.android.exoplayer2.source.MediaPeriod
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        long j;
        boolean z;
        int i;
        this.sampleStreams = new ClippingSampleStream[streams.length];
        SampleStream[] childStreams = new SampleStream[streams.length];
        int i2 = 0;
        while (true) {
            SampleStream sampleStream = null;
            if (i2 >= streams.length) {
                break;
            }
            ClippingSampleStream[] clippingSampleStreamArr = this.sampleStreams;
            clippingSampleStreamArr[i2] = (ClippingSampleStream) streams[i2];
            if (clippingSampleStreamArr[i2] != null) {
                sampleStream = clippingSampleStreamArr[i2].childStream;
            }
            childStreams[i2] = sampleStream;
            i2++;
        }
        long enablePositionUs = this.mediaPeriod.selectTracks(selections, mayRetainStreamFlags, childStreams, streamResetFlags, positionUs);
        if (isPendingInitialDiscontinuity()) {
            long j2 = this.startUs;
            if (positionUs == j2 && shouldKeepInitialDiscontinuity(j2, selections)) {
                j = enablePositionUs;
                this.pendingInitialDiscontinuityPositionUs = j;
                if (enablePositionUs != positionUs) {
                    if (enablePositionUs >= this.startUs) {
                        long j3 = this.endUs;
                        if (j3 != Long.MIN_VALUE) {
                        }
                    }
                    z = false;
                    Assertions.checkState(z);
                    for (i = 0; i < streams.length; i++) {
                        if (childStreams[i] == null) {
                            this.sampleStreams[i] = null;
                        } else {
                            ClippingSampleStream[] clippingSampleStreamArr2 = this.sampleStreams;
                            if (clippingSampleStreamArr2[i] == null || clippingSampleStreamArr2[i].childStream != childStreams[i]) {
                                this.sampleStreams[i] = new ClippingSampleStream(childStreams[i]);
                            }
                        }
                        streams[i] = this.sampleStreams[i];
                    }
                    return enablePositionUs;
                }
                z = true;
                Assertions.checkState(z);
                while (i < streams.length) {
                }
                return enablePositionUs;
            }
        }
        j = C.TIME_UNSET;
        this.pendingInitialDiscontinuityPositionUs = j;
        if (enablePositionUs != positionUs) {
        }
        z = true;
        Assertions.checkState(z);
        while (i < streams.length) {
        }
        return enablePositionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs, boolean toKeyframe) {
        this.mediaPeriod.discardBuffer(positionUs, toKeyframe);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public void reevaluateBuffer(long positionUs) {
        this.mediaPeriod.reevaluateBuffer(positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        if (isPendingInitialDiscontinuity()) {
            long initialDiscontinuityUs = this.pendingInitialDiscontinuityPositionUs;
            this.pendingInitialDiscontinuityPositionUs = C.TIME_UNSET;
            long childDiscontinuityUs = readDiscontinuity();
            return childDiscontinuityUs != C.TIME_UNSET ? childDiscontinuityUs : initialDiscontinuityUs;
        }
        long discontinuityUs = this.mediaPeriod.readDiscontinuity();
        if (discontinuityUs == C.TIME_UNSET) {
            return C.TIME_UNSET;
        }
        boolean z = true;
        Assertions.checkState(discontinuityUs >= this.startUs);
        long j = this.endUs;
        if (j != Long.MIN_VALUE && discontinuityUs > j) {
            z = false;
        }
        Assertions.checkState(z);
        return discontinuityUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getBufferedPositionUs() {
        long bufferedPositionUs = this.mediaPeriod.getBufferedPositionUs();
        if (bufferedPositionUs != Long.MIN_VALUE) {
            long j = this.endUs;
            if (j == Long.MIN_VALUE || bufferedPositionUs < j) {
                return bufferedPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0032, code lost:
        if (r0 > r3) goto L17;
     */
    @Override // com.google.android.exoplayer2.source.MediaPeriod
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long seekToUs(long positionUs) {
        ClippingSampleStream[] clippingSampleStreamArr;
        this.pendingInitialDiscontinuityPositionUs = C.TIME_UNSET;
        boolean z = false;
        for (ClippingSampleStream sampleStream : this.sampleStreams) {
            if (sampleStream != null) {
                sampleStream.clearSentEos();
            }
        }
        long seekUs = this.mediaPeriod.seekToUs(positionUs);
        if (seekUs != positionUs) {
            if (seekUs >= this.startUs) {
                long j = this.endUs;
                if (j != Long.MIN_VALUE) {
                }
            }
            Assertions.checkState(z);
            return seekUs;
        }
        z = true;
        Assertions.checkState(z);
        return seekUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long positionUs, SeekParameters seekParameters) {
        long j = this.startUs;
        if (positionUs == j) {
            return j;
        }
        SeekParameters clippedSeekParameters = clipSeekParameters(positionUs, seekParameters);
        return this.mediaPeriod.getAdjustedSeekPositionUs(positionUs, clippedSeekParameters);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long nextLoadPositionUs = this.mediaPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs != Long.MIN_VALUE) {
            long j = this.endUs;
            if (j == Long.MIN_VALUE || nextLoadPositionUs < j) {
                return nextLoadPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        return this.mediaPeriod.continueLoading(positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean isLoading() {
        return this.mediaPeriod.isLoading();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod mediaPeriod) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onPrepared(this);
    }

    public void onContinueLoadingRequested(MediaPeriod source) {
        ((MediaPeriod.Callback) Assertions.checkNotNull(this.callback)).onContinueLoadingRequested(this);
    }

    boolean isPendingInitialDiscontinuity() {
        return this.pendingInitialDiscontinuityPositionUs != C.TIME_UNSET;
    }

    private SeekParameters clipSeekParameters(long positionUs, SeekParameters seekParameters) {
        long toleranceBeforeUs = Util.constrainValue(seekParameters.toleranceBeforeUs, 0L, positionUs - this.startUs);
        long j = seekParameters.toleranceAfterUs;
        long j2 = this.endUs;
        long toleranceAfterUs = Util.constrainValue(j, 0L, j2 == Long.MIN_VALUE ? Long.MAX_VALUE : j2 - positionUs);
        if (toleranceBeforeUs == seekParameters.toleranceBeforeUs && toleranceAfterUs == seekParameters.toleranceAfterUs) {
            return seekParameters;
        }
        return new SeekParameters(toleranceBeforeUs, toleranceAfterUs);
    }

    private static boolean shouldKeepInitialDiscontinuity(long startUs, TrackSelection[] selections) {
        if (startUs != 0) {
            for (TrackSelection trackSelection : selections) {
                if (trackSelection != null) {
                    Format selectedFormat = trackSelection.getSelectedFormat();
                    if (!MimeTypes.isAudio(selectedFormat.sampleMimeType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class ClippingSampleStream implements SampleStream {
        public final SampleStream childStream;
        private boolean sentEos;

        public ClippingSampleStream(SampleStream childStream) {
            ClippingMediaPeriod.this = r1;
            this.childStream = childStream;
        }

        public void clearSentEos() {
            this.sentEos = false;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public boolean isReady() {
            return !ClippingMediaPeriod.this.isPendingInitialDiscontinuity() && this.childStream.isReady();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.childStream.maybeThrowError();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean requireFormat) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            if (this.sentEos) {
                buffer.setFlags(4);
                return -4;
            }
            int result = this.childStream.readData(formatHolder, buffer, requireFormat);
            if (result == -5) {
                Format format = (Format) Assertions.checkNotNull(formatHolder.format);
                if (format.encoderDelay != 0 || format.encoderPadding != 0) {
                    int i = 0;
                    int encoderDelay = ClippingMediaPeriod.this.startUs != 0 ? 0 : format.encoderDelay;
                    if (ClippingMediaPeriod.this.endUs == Long.MIN_VALUE) {
                        i = format.encoderPadding;
                    }
                    int encoderPadding = i;
                    formatHolder.format = format.copyWithGaplessInfo(encoderDelay, encoderPadding);
                }
                return -5;
            } else if (ClippingMediaPeriod.this.endUs != Long.MIN_VALUE && ((result == -4 && buffer.timeUs >= ClippingMediaPeriod.this.endUs) || (result == -3 && ClippingMediaPeriod.this.getBufferedPositionUs() == Long.MIN_VALUE && !buffer.waitingForKeys))) {
                buffer.clear();
                buffer.setFlags(4);
                this.sentEos = true;
                return -4;
            } else {
                return result;
            }
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int skipData(long positionUs) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            return this.childStream.skipData(positionUs);
        }
    }
}
