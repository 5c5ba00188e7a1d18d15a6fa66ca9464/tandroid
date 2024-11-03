package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.FileLog;

/* loaded from: classes.dex */
public class AdaptiveTrackSelection extends BaseTrackSelection {
    private final ImmutableList adaptationCheckpoints;
    private final float bandwidthFraction;
    private final BandwidthMeter bandwidthMeter;
    private final float bufferedFractionToLiveEdgeForQualityIncrease;
    private final Clock clock;
    private MediaChunk lastBufferEvaluationMediaChunk;
    private long lastBufferEvaluationMs;
    private final long maxDurationForQualityDecreaseUs;
    private final int maxHeightToDiscard;
    private final int maxWidthToDiscard;
    private final long minDurationForQualityIncreaseUs;
    private final long minDurationToRetainAfterDiscardUs;
    private float playbackSpeed;
    private int reason;
    private int selectedIndex;

    /* loaded from: classes.dex */
    public static final class AdaptationCheckpoint {
        public final long allocatedBandwidth;
        public final long totalBandwidth;

        public AdaptationCheckpoint(long j, long j2) {
            this.totalBandwidth = j;
            this.allocatedBandwidth = j2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AdaptationCheckpoint)) {
                return false;
            }
            AdaptationCheckpoint adaptationCheckpoint = (AdaptationCheckpoint) obj;
            return this.totalBandwidth == adaptationCheckpoint.totalBandwidth && this.allocatedBandwidth == adaptationCheckpoint.allocatedBandwidth;
        }

        public int hashCode() {
            return (((int) this.totalBandwidth) * 31) + ((int) this.allocatedBandwidth);
        }
    }

    /* loaded from: classes.dex */
    public static class Factory implements ExoTrackSelection.Factory {
        private final float bandwidthFraction;
        private final float bufferedFractionToLiveEdgeForQualityIncrease;
        private final Clock clock;
        private final int maxDurationForQualityDecreaseMs;
        private final int maxHeightToDiscard;
        private final int maxWidthToDiscard;
        private final int minDurationForQualityIncreaseMs;
        private final int minDurationToRetainAfterDiscardMs;

        public Factory() {
            this(10000, 25000, 25000, 0.7f);
        }

        public Factory(int i, int i2, int i3, float f) {
            this(i, i2, i3, 1279, 719, f, 0.75f, Clock.DEFAULT);
        }

        public Factory(int i, int i2, int i3, int i4, int i5, float f, float f2, Clock clock) {
            this.minDurationForQualityIncreaseMs = i;
            this.maxDurationForQualityDecreaseMs = i2;
            this.minDurationToRetainAfterDiscardMs = i3;
            this.maxWidthToDiscard = i4;
            this.maxHeightToDiscard = i5;
            this.bandwidthFraction = f;
            this.bufferedFractionToLiveEdgeForQualityIncrease = f2;
            this.clock = clock;
        }

        protected AdaptiveTrackSelection createAdaptiveTrackSelection(TrackGroup trackGroup, int[] iArr, int i, BandwidthMeter bandwidthMeter, ImmutableList immutableList) {
            return new AdaptiveTrackSelection(trackGroup, iArr, i, bandwidthMeter, this.minDurationForQualityIncreaseMs, this.maxDurationForQualityDecreaseMs, this.minDurationToRetainAfterDiscardMs, this.maxWidthToDiscard, this.maxHeightToDiscard, this.bandwidthFraction, this.bufferedFractionToLiveEdgeForQualityIncrease, immutableList, this.clock);
        }

        @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection.Factory
        public final ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] definitionArr, BandwidthMeter bandwidthMeter, MediaSource.MediaPeriodId mediaPeriodId, Timeline timeline) {
            ImmutableList adaptationCheckpoints = AdaptiveTrackSelection.getAdaptationCheckpoints(definitionArr);
            ExoTrackSelection[] exoTrackSelectionArr = new ExoTrackSelection[definitionArr.length];
            for (int i = 0; i < definitionArr.length; i++) {
                ExoTrackSelection.Definition definition = definitionArr[i];
                if (definition != null) {
                    int[] iArr = definition.tracks;
                    if (iArr.length != 0) {
                        exoTrackSelectionArr[i] = iArr.length == 1 ? new FixedTrackSelection(definition.group, iArr[0], definition.type) : createAdaptiveTrackSelection(definition.group, iArr, definition.type, bandwidthMeter, (ImmutableList) adaptationCheckpoints.get(i));
                    }
                }
            }
            return exoTrackSelectionArr;
        }
    }

    protected AdaptiveTrackSelection(TrackGroup trackGroup, int[] iArr, int i, BandwidthMeter bandwidthMeter, long j, long j2, long j3, int i2, int i3, float f, float f2, List list, Clock clock) {
        super(trackGroup, iArr, i);
        BandwidthMeter bandwidthMeter2;
        long j4;
        if (j3 < j) {
            Log.w("AdaptiveTrackSelection", "Adjusting minDurationToRetainAfterDiscardMs to be at least minDurationForQualityIncreaseMs");
            bandwidthMeter2 = bandwidthMeter;
            j4 = j;
        } else {
            bandwidthMeter2 = bandwidthMeter;
            j4 = j3;
        }
        this.bandwidthMeter = bandwidthMeter2;
        this.minDurationForQualityIncreaseUs = j * 1000;
        this.maxDurationForQualityDecreaseUs = j2 * 1000;
        this.minDurationToRetainAfterDiscardUs = j4 * 1000;
        this.maxWidthToDiscard = i2;
        this.maxHeightToDiscard = i3;
        this.bandwidthFraction = f;
        this.bufferedFractionToLiveEdgeForQualityIncrease = f2;
        this.adaptationCheckpoints = ImmutableList.copyOf((Collection) list);
        this.clock = clock;
        this.playbackSpeed = 1.0f;
        this.reason = 0;
        this.lastBufferEvaluationMs = -9223372036854775807L;
    }

    private static void addCheckpoint(List list, long[] jArr) {
        long j = 0;
        for (long j2 : jArr) {
            j += j2;
        }
        for (int i = 0; i < list.size(); i++) {
            ImmutableList.Builder builder = (ImmutableList.Builder) list.get(i);
            if (builder != null) {
                builder.add((Object) new AdaptationCheckpoint(j, jArr[i]));
            }
        }
    }

    private int determineIdealSelectedIndex(int i, long j, long j2) {
        StringBuilder sb;
        long allocatedBandwidth = getAllocatedBandwidth(j2);
        FileLog.d("debug_loading_player: determineIdealSelectedIndex: type=" + i + " effectiveBitrate=" + allocatedBandwidth);
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < this.length; i3++) {
            if (j == Long.MIN_VALUE || !isBlacklisted(i3, j)) {
                Format format = getFormat(i3);
                int max = Math.max(format.width, format.height);
                if (hashMap.containsKey(Integer.valueOf(max))) {
                    Integer num = (Integer) hashMap.get(Integer.valueOf(max));
                    Format format2 = getFormat(num.intValue());
                    boolean z = format2.cached;
                    if ((!z || format.cached) && ((!z && format.cached) || format.bitrate < format2.bitrate)) {
                        hashMap.put(Integer.valueOf(max), Integer.valueOf(i3));
                        arrayList.remove(num);
                    }
                } else {
                    hashMap.put(Integer.valueOf(max), Integer.valueOf(i3));
                }
                arrayList.add(Integer.valueOf(i3));
            }
        }
        if (i == 0) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                if (getFormat(intValue).cached) {
                    FileLog.d("debug_loading_player: determineIdealSelectedIndex: initial setup, choose cached format#" + intValue);
                    return intValue;
                }
            }
        }
        Iterator it2 = arrayList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                sb = new StringBuilder();
                sb.append("debug_loading_player: determineIdealSelectedIndex: selected format#");
                sb.append(i2);
                sb.append(" (lowest, nothing is fit)");
                break;
            }
            i2 = ((Integer) it2.next()).intValue();
            Format format3 = getFormat(i2);
            FileLog.d("debug_loading_player: determineIdealSelectedIndex: format#" + i2 + " bitrate=" + format3.bitrate + " " + format3.width + "x" + format3.height + " codecs=" + format3.codecs + " (cached=" + format3.cached + ")");
            if (canSelectFormat(format3, format3.bitrate, allocatedBandwidth)) {
                sb = new StringBuilder();
                sb.append("debug_loading_player: determineIdealSelectedIndex: selected format#");
                sb.append(i2);
                break;
            }
        }
        FileLog.d(sb.toString());
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImmutableList getAdaptationCheckpoints(ExoTrackSelection.Definition[] definitionArr) {
        ImmutableList.Builder builder;
        ArrayList arrayList = new ArrayList();
        for (ExoTrackSelection.Definition definition : definitionArr) {
            if (definition == null || definition.tracks.length <= 1) {
                builder = null;
            } else {
                builder = ImmutableList.builder();
                builder.add((Object) new AdaptationCheckpoint(0L, 0L));
            }
            arrayList.add(builder);
        }
        long[][] sortedTrackBitrates = getSortedTrackBitrates(definitionArr);
        int[] iArr = new int[sortedTrackBitrates.length];
        long[] jArr = new long[sortedTrackBitrates.length];
        for (int i = 0; i < sortedTrackBitrates.length; i++) {
            long[] jArr2 = sortedTrackBitrates[i];
            jArr[i] = jArr2.length == 0 ? 0L : jArr2[0];
        }
        addCheckpoint(arrayList, jArr);
        ImmutableList switchOrder = getSwitchOrder(sortedTrackBitrates);
        for (int i2 = 0; i2 < switchOrder.size(); i2++) {
            int intValue = ((Integer) switchOrder.get(i2)).intValue();
            int i3 = iArr[intValue] + 1;
            iArr[intValue] = i3;
            jArr[intValue] = sortedTrackBitrates[intValue][i3];
            addCheckpoint(arrayList, jArr);
        }
        for (int i4 = 0; i4 < definitionArr.length; i4++) {
            if (arrayList.get(i4) != null) {
                jArr[i4] = jArr[i4] * 2;
            }
        }
        addCheckpoint(arrayList, jArr);
        ImmutableList.Builder builder2 = ImmutableList.builder();
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            ImmutableList.Builder builder3 = (ImmutableList.Builder) arrayList.get(i5);
            builder2.add((Object) (builder3 == null ? ImmutableList.of() : builder3.build()));
        }
        return builder2.build();
    }

    private long getAllocatedBandwidth(long j) {
        long totalAllocatableBandwidth = getTotalAllocatableBandwidth(j);
        if (this.adaptationCheckpoints.isEmpty()) {
            return totalAllocatableBandwidth;
        }
        int i = 1;
        while (i < this.adaptationCheckpoints.size() - 1 && ((AdaptationCheckpoint) this.adaptationCheckpoints.get(i)).totalBandwidth < totalAllocatableBandwidth) {
            i++;
        }
        AdaptationCheckpoint adaptationCheckpoint = (AdaptationCheckpoint) this.adaptationCheckpoints.get(i - 1);
        AdaptationCheckpoint adaptationCheckpoint2 = (AdaptationCheckpoint) this.adaptationCheckpoints.get(i);
        long j2 = adaptationCheckpoint.totalBandwidth;
        float f = ((float) (totalAllocatableBandwidth - j2)) / ((float) (adaptationCheckpoint2.totalBandwidth - j2));
        return adaptationCheckpoint.allocatedBandwidth + (f * ((float) (adaptationCheckpoint2.allocatedBandwidth - r2)));
    }

    private long getLastChunkDurationUs(List list) {
        if (list.isEmpty()) {
            return -9223372036854775807L;
        }
        MediaChunk mediaChunk = (MediaChunk) Iterables.getLast(list);
        long j = mediaChunk.startTimeUs;
        if (j == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        long j2 = mediaChunk.endTimeUs;
        if (j2 != -9223372036854775807L) {
            return j2 - j;
        }
        return -9223372036854775807L;
    }

    private long getNextChunkDurationUs(MediaChunkIterator[] mediaChunkIteratorArr, List list) {
        int i = this.selectedIndex;
        if (i < mediaChunkIteratorArr.length && mediaChunkIteratorArr[i].next()) {
            MediaChunkIterator mediaChunkIterator = mediaChunkIteratorArr[this.selectedIndex];
            return mediaChunkIterator.getChunkEndTimeUs() - mediaChunkIterator.getChunkStartTimeUs();
        }
        for (MediaChunkIterator mediaChunkIterator2 : mediaChunkIteratorArr) {
            if (mediaChunkIterator2.next()) {
                return mediaChunkIterator2.getChunkEndTimeUs() - mediaChunkIterator2.getChunkStartTimeUs();
            }
        }
        return getLastChunkDurationUs(list);
    }

    private static long[][] getSortedTrackBitrates(ExoTrackSelection.Definition[] definitionArr) {
        long[][] jArr = new long[definitionArr.length];
        for (int i = 0; i < definitionArr.length; i++) {
            ExoTrackSelection.Definition definition = definitionArr[i];
            if (definition == null) {
                jArr[i] = new long[0];
            } else {
                jArr[i] = new long[definition.tracks.length];
                int i2 = 0;
                while (true) {
                    int[] iArr = definition.tracks;
                    if (i2 >= iArr.length) {
                        break;
                    }
                    long j = definition.group.getFormat(iArr[i2]).bitrate;
                    long[] jArr2 = jArr[i];
                    if (j == -1) {
                        j = 0;
                    }
                    jArr2[i2] = j;
                    i2++;
                }
                Arrays.sort(jArr[i]);
            }
        }
        return jArr;
    }

    private static ImmutableList getSwitchOrder(long[][] jArr) {
        ListMultimap build = MultimapBuilder.treeKeys().arrayListValues().build();
        for (int i = 0; i < jArr.length; i++) {
            long[] jArr2 = jArr[i];
            if (jArr2.length > 1) {
                int length = jArr2.length;
                double[] dArr = new double[length];
                int i2 = 0;
                while (true) {
                    long[] jArr3 = jArr[i];
                    double d = 0.0d;
                    if (i2 >= jArr3.length) {
                        break;
                    }
                    long j = jArr3[i2];
                    if (j != -1) {
                        d = Math.log(j);
                    }
                    dArr[i2] = d;
                    i2++;
                }
                int i3 = length - 1;
                double d2 = dArr[i3] - dArr[0];
                int i4 = 0;
                while (i4 < i3) {
                    double d3 = dArr[i4];
                    i4++;
                    build.put(Double.valueOf(d2 == 0.0d ? 1.0d : (((d3 + dArr[i4]) * 0.5d) - dArr[0]) / d2), Integer.valueOf(i));
                }
            }
        }
        return ImmutableList.copyOf(build.values());
    }

    private long getTotalAllocatableBandwidth(long j) {
        long bitrateEstimate = ((float) this.bandwidthMeter.getBitrateEstimate()) * this.bandwidthFraction;
        if (this.bandwidthMeter.getTimeToFirstByteEstimateUs() == -9223372036854775807L || j == -9223372036854775807L) {
            return ((float) bitrateEstimate) / this.playbackSpeed;
        }
        float f = (float) j;
        return (((float) bitrateEstimate) * Math.max((f / this.playbackSpeed) - ((float) r2), 0.0f)) / f;
    }

    private long minDurationForQualityIncreaseUs(long j, long j2) {
        if (j == -9223372036854775807L) {
            return this.minDurationForQualityIncreaseUs;
        }
        if (j2 != -9223372036854775807L) {
            j -= j2;
        }
        return Math.min(((float) j) * this.bufferedFractionToLiveEdgeForQualityIncrease, this.minDurationForQualityIncreaseUs);
    }

    protected boolean canSelectFormat(Format format, int i, long j) {
        return format.cached || ((long) i) <= j;
    }

    @Override // com.google.android.exoplayer2.trackselection.BaseTrackSelection, com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public void disable() {
        this.lastBufferEvaluationMediaChunk = null;
    }

    @Override // com.google.android.exoplayer2.trackselection.BaseTrackSelection, com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public void enable() {
        this.lastBufferEvaluationMs = -9223372036854775807L;
        this.lastBufferEvaluationMediaChunk = null;
    }

    @Override // com.google.android.exoplayer2.trackselection.BaseTrackSelection, com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int evaluateQueueSize(long j, List list) {
        int i;
        int i2;
        long elapsedRealtime = this.clock.elapsedRealtime();
        if (!shouldEvaluateQueueSize(elapsedRealtime, list)) {
            return list.size();
        }
        this.lastBufferEvaluationMs = elapsedRealtime;
        this.lastBufferEvaluationMediaChunk = list.isEmpty() ? null : (MediaChunk) Iterables.getLast(list);
        if (list.isEmpty()) {
            return 0;
        }
        int size = list.size();
        long playoutDurationForMediaDuration = Util.getPlayoutDurationForMediaDuration(((MediaChunk) list.get(size - 1)).startTimeUs - j, this.playbackSpeed);
        long minDurationToRetainAfterDiscardUs = getMinDurationToRetainAfterDiscardUs();
        if (playoutDurationForMediaDuration < minDurationToRetainAfterDiscardUs) {
            return size;
        }
        Format format = getFormat(determineIdealSelectedIndex(-1, elapsedRealtime, getLastChunkDurationUs(list)));
        for (int i3 = 0; i3 < size; i3++) {
            MediaChunk mediaChunk = (MediaChunk) list.get(i3);
            Format format2 = mediaChunk.trackFormat;
            if (Util.getPlayoutDurationForMediaDuration(mediaChunk.startTimeUs - j, this.playbackSpeed) >= minDurationToRetainAfterDiscardUs && format2.bitrate < format.bitrate && (i = format2.height) != -1 && i <= this.maxHeightToDiscard && (i2 = format2.width) != -1 && i2 <= this.maxWidthToDiscard && i < format.height) {
                return i3;
            }
        }
        return size;
    }

    protected long getMinDurationToRetainAfterDiscardUs() {
        return this.minDurationToRetainAfterDiscardUs;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public Object getSelectionData() {
        return null;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int getSelectionReason() {
        return this.reason;
    }

    @Override // com.google.android.exoplayer2.trackselection.BaseTrackSelection, com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public void onPlaybackSpeed(float f) {
        this.playbackSpeed = f;
    }

    protected boolean shouldEvaluateQueueSize(long j, List list) {
        long j2 = this.lastBufferEvaluationMs;
        return j2 == -9223372036854775807L || j - j2 >= 1000 || !(list.isEmpty() || ((MediaChunk) Iterables.getLast(list)).equals(this.lastBufferEvaluationMediaChunk));
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public void updateSelectedTrack(long j, long j2, long j3, List list, MediaChunkIterator[] mediaChunkIteratorArr) {
        int i;
        int i2;
        int determineIdealSelectedIndex;
        long elapsedRealtime = this.clock.elapsedRealtime();
        long nextChunkDurationUs = getNextChunkDurationUs(mediaChunkIteratorArr, list);
        int i3 = this.reason;
        if (i3 == 0) {
            this.reason = 1;
            determineIdealSelectedIndex = determineIdealSelectedIndex(0, elapsedRealtime, nextChunkDurationUs);
        } else {
            int i4 = this.selectedIndex;
            int indexOf = list.isEmpty() ? -1 : indexOf(((MediaChunk) Iterables.getLast(list)).trackFormat);
            if (indexOf != -1) {
                i = ((MediaChunk) Iterables.getLast(list)).trackSelectionReason;
                i2 = indexOf;
            } else {
                i = i3;
                i2 = i4;
            }
            determineIdealSelectedIndex = determineIdealSelectedIndex(1, elapsedRealtime, nextChunkDurationUs);
            if (!isBlacklisted(i2, elapsedRealtime)) {
                Format format = getFormat(i2);
                Format format2 = getFormat(determineIdealSelectedIndex);
                long minDurationForQualityIncreaseUs = minDurationForQualityIncreaseUs(j3, nextChunkDurationUs);
                int i5 = format2.bitrate;
                int i6 = format.bitrate;
                if ((i5 > i6 && j2 < minDurationForQualityIncreaseUs) || (i5 < i6 && j2 >= this.maxDurationForQualityDecreaseUs)) {
                    determineIdealSelectedIndex = i2;
                }
            }
            if (determineIdealSelectedIndex != i2) {
                i = 3;
            }
            this.reason = i;
        }
        this.selectedIndex = determineIdealSelectedIndex;
    }
}
