package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Consumer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import org.telegram.messenger.MediaDataController;

/* loaded from: classes.dex */
public class SampleQueue implements TrackOutput {
    private int absoluteFirstIndex;
    private DrmSession currentDrmSession;
    private Format downstreamFormat;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private boolean isLastSampleQueued;
    private int length;
    private boolean loggedUnexpectedNonSyncSample;
    private boolean pendingSplice;
    private int readPosition;
    private int relativeFirstIndex;
    private final SampleDataQueue sampleDataQueue;
    private long sampleOffsetUs;
    private Format unadjustedUpstreamFormat;
    private boolean upstreamAllSamplesAreSyncSamples;
    private Format upstreamFormat;
    private boolean upstreamFormatAdjustmentRequired;
    private UpstreamFormatChangedListener upstreamFormatChangeListener;
    private int upstreamSourceId;
    private final SampleExtrasHolder extrasHolder = new SampleExtrasHolder();
    private int capacity = MediaDataController.MAX_STYLE_RUNS_COUNT;
    private int[] sourceIds = new int[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private long[] offsets = new long[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private long[] timesUs = new long[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private int[] flags = new int[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private int[] sizes = new int[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private TrackOutput.CryptoData[] cryptoDatas = new TrackOutput.CryptoData[MediaDataController.MAX_STYLE_RUNS_COUNT];
    private final SpannedData sharedSampleMetadata = new SpannedData(new Consumer() { // from class: com.google.android.exoplayer2.source.SampleQueue$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.util.Consumer
        public final void accept(Object obj) {
            SampleQueue.lambda$new$0((SampleQueue.SharedSampleMetadata) obj);
        }
    });
    private long startTimeUs = Long.MIN_VALUE;
    private long largestDiscardedTimestampUs = Long.MIN_VALUE;
    private long largestQueuedTimestampUs = Long.MIN_VALUE;
    private boolean upstreamFormatRequired = true;
    private boolean upstreamKeyframeRequired = true;

    static final class SampleExtrasHolder {
        public TrackOutput.CryptoData cryptoData;
        public long offset;
        public int size;

        SampleExtrasHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SharedSampleMetadata {
        public final DrmSessionManager.DrmSessionReference drmSessionReference;
        public final Format format;

        private SharedSampleMetadata(Format format, DrmSessionManager.DrmSessionReference drmSessionReference) {
            this.format = format;
            this.drmSessionReference = drmSessionReference;
        }
    }

    public interface UpstreamFormatChangedListener {
        void onUpstreamFormatChanged(Format format);
    }

    protected SampleQueue(Allocator allocator, DrmSessionManager drmSessionManager, DrmSessionEventListener.EventDispatcher eventDispatcher) {
        this.drmSessionManager = drmSessionManager;
        this.drmEventDispatcher = eventDispatcher;
        this.sampleDataQueue = new SampleDataQueue(allocator);
    }

    private synchronized boolean attemptSplice(long j) {
        if (this.length == 0) {
            return j > this.largestDiscardedTimestampUs;
        }
        if (getLargestReadTimestampUs() >= j) {
            return false;
        }
        discardUpstreamSampleMetadata(this.absoluteFirstIndex + countUnreadSamplesBefore(j));
        return true;
    }

    private synchronized void commitSample(long j, int i, long j2, int i2, TrackOutput.CryptoData cryptoData) {
        try {
            int i3 = this.length;
            if (i3 > 0) {
                int relativeIndex = getRelativeIndex(i3 - 1);
                Assertions.checkArgument(this.offsets[relativeIndex] + ((long) this.sizes[relativeIndex]) <= j2);
            }
            this.isLastSampleQueued = (536870912 & i) != 0;
            this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, j);
            int relativeIndex2 = getRelativeIndex(this.length);
            this.timesUs[relativeIndex2] = j;
            this.offsets[relativeIndex2] = j2;
            this.sizes[relativeIndex2] = i2;
            this.flags[relativeIndex2] = i;
            this.cryptoDatas[relativeIndex2] = cryptoData;
            this.sourceIds[relativeIndex2] = this.upstreamSourceId;
            if (this.sharedSampleMetadata.isEmpty() || !((SharedSampleMetadata) this.sharedSampleMetadata.getEndValue()).format.equals(this.upstreamFormat)) {
                DrmSessionManager drmSessionManager = this.drmSessionManager;
                this.sharedSampleMetadata.appendSpan(getWriteIndex(), new SharedSampleMetadata((Format) Assertions.checkNotNull(this.upstreamFormat), drmSessionManager != null ? drmSessionManager.preacquireSession(this.drmEventDispatcher, this.upstreamFormat) : DrmSessionManager.DrmSessionReference.EMPTY));
            }
            int i4 = this.length + 1;
            this.length = i4;
            int i5 = this.capacity;
            if (i4 == i5) {
                int i6 = i5 + MediaDataController.MAX_STYLE_RUNS_COUNT;
                int[] iArr = new int[i6];
                long[] jArr = new long[i6];
                long[] jArr2 = new long[i6];
                int[] iArr2 = new int[i6];
                int[] iArr3 = new int[i6];
                TrackOutput.CryptoData[] cryptoDataArr = new TrackOutput.CryptoData[i6];
                int i7 = this.relativeFirstIndex;
                int i8 = i5 - i7;
                System.arraycopy(this.offsets, i7, jArr, 0, i8);
                System.arraycopy(this.timesUs, this.relativeFirstIndex, jArr2, 0, i8);
                System.arraycopy(this.flags, this.relativeFirstIndex, iArr2, 0, i8);
                System.arraycopy(this.sizes, this.relativeFirstIndex, iArr3, 0, i8);
                System.arraycopy(this.cryptoDatas, this.relativeFirstIndex, cryptoDataArr, 0, i8);
                System.arraycopy(this.sourceIds, this.relativeFirstIndex, iArr, 0, i8);
                int i9 = this.relativeFirstIndex;
                System.arraycopy(this.offsets, 0, jArr, i8, i9);
                System.arraycopy(this.timesUs, 0, jArr2, i8, i9);
                System.arraycopy(this.flags, 0, iArr2, i8, i9);
                System.arraycopy(this.sizes, 0, iArr3, i8, i9);
                System.arraycopy(this.cryptoDatas, 0, cryptoDataArr, i8, i9);
                System.arraycopy(this.sourceIds, 0, iArr, i8, i9);
                this.offsets = jArr;
                this.timesUs = jArr2;
                this.flags = iArr2;
                this.sizes = iArr3;
                this.cryptoDatas = cryptoDataArr;
                this.sourceIds = iArr;
                this.relativeFirstIndex = 0;
                this.capacity = i6;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private int countUnreadSamplesBefore(long j) {
        int i = this.length;
        int relativeIndex = getRelativeIndex(i - 1);
        while (i > this.readPosition && this.timesUs[relativeIndex] >= j) {
            i--;
            relativeIndex--;
            if (relativeIndex == -1) {
                relativeIndex = this.capacity - 1;
            }
        }
        return i;
    }

    public static SampleQueue createWithDrm(Allocator allocator, DrmSessionManager drmSessionManager, DrmSessionEventListener.EventDispatcher eventDispatcher) {
        return new SampleQueue(allocator, (DrmSessionManager) Assertions.checkNotNull(drmSessionManager), (DrmSessionEventListener.EventDispatcher) Assertions.checkNotNull(eventDispatcher));
    }

    public static SampleQueue createWithoutDrm(Allocator allocator) {
        return new SampleQueue(allocator, null, null);
    }

    private synchronized long discardSampleMetadataTo(long j, boolean z, boolean z2) {
        int i;
        try {
            int i2 = this.length;
            if (i2 != 0) {
                long[] jArr = this.timesUs;
                int i3 = this.relativeFirstIndex;
                if (j >= jArr[i3]) {
                    if (z2 && (i = this.readPosition) != i2) {
                        i2 = i + 1;
                    }
                    int findSampleBefore = findSampleBefore(i3, i2, j, z);
                    if (findSampleBefore == -1) {
                        return -1L;
                    }
                    return discardSamples(findSampleBefore);
                }
            }
            return -1L;
        } finally {
        }
    }

    private synchronized long discardSampleMetadataToEnd() {
        int i = this.length;
        if (i == 0) {
            return -1L;
        }
        return discardSamples(i);
    }

    private long discardSamples(int i) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(i));
        this.length -= i;
        int i2 = this.absoluteFirstIndex + i;
        this.absoluteFirstIndex = i2;
        int i3 = this.relativeFirstIndex + i;
        this.relativeFirstIndex = i3;
        int i4 = this.capacity;
        if (i3 >= i4) {
            this.relativeFirstIndex = i3 - i4;
        }
        int i5 = this.readPosition - i;
        this.readPosition = i5;
        if (i5 < 0) {
            this.readPosition = 0;
        }
        this.sharedSampleMetadata.discardTo(i2);
        if (this.length != 0) {
            return this.offsets[this.relativeFirstIndex];
        }
        int i6 = this.relativeFirstIndex;
        if (i6 == 0) {
            i6 = this.capacity;
        }
        return this.offsets[i6 - 1] + this.sizes[r6];
    }

    private long discardUpstreamSampleMetadata(int i) {
        int writeIndex = getWriteIndex() - i;
        boolean z = false;
        Assertions.checkArgument(writeIndex >= 0 && writeIndex <= this.length - this.readPosition);
        int i2 = this.length - writeIndex;
        this.length = i2;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(i2));
        if (writeIndex == 0 && this.isLastSampleQueued) {
            z = true;
        }
        this.isLastSampleQueued = z;
        this.sharedSampleMetadata.discardFrom(i);
        int i3 = this.length;
        if (i3 == 0) {
            return 0L;
        }
        return this.offsets[getRelativeIndex(i3 - 1)] + this.sizes[r9];
    }

    private int findSampleBefore(int i, int i2, long j, boolean z) {
        int i3 = -1;
        for (int i4 = 0; i4 < i2; i4++) {
            long j2 = this.timesUs[i];
            if (j2 > j) {
                return i3;
            }
            if (!z || (this.flags[i] & 1) != 0) {
                if (j2 == j) {
                    return i4;
                }
                i3 = i4;
            }
            i++;
            if (i == this.capacity) {
                i = 0;
            }
        }
        return i3;
    }

    private long getLargestTimestamp(int i) {
        long j = Long.MIN_VALUE;
        if (i == 0) {
            return Long.MIN_VALUE;
        }
        int relativeIndex = getRelativeIndex(i - 1);
        for (int i2 = 0; i2 < i; i2++) {
            j = Math.max(j, this.timesUs[relativeIndex]);
            if ((this.flags[relativeIndex] & 1) != 0) {
                break;
            }
            relativeIndex--;
            if (relativeIndex == -1) {
                relativeIndex = this.capacity - 1;
            }
        }
        return j;
    }

    private int getRelativeIndex(int i) {
        int i2 = this.relativeFirstIndex + i;
        int i3 = this.capacity;
        return i2 < i3 ? i2 : i2 - i3;
    }

    private boolean hasNextSample() {
        return this.readPosition != this.length;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(SharedSampleMetadata sharedSampleMetadata) {
        sharedSampleMetadata.drmSessionReference.release();
    }

    private boolean mayReadSample(int i) {
        DrmSession drmSession = this.currentDrmSession;
        return drmSession == null || drmSession.getState() == 4 || ((this.flags[i] & 1073741824) == 0 && this.currentDrmSession.playClearSamplesWithoutKeys());
    }

    private void onFormatResult(Format format, FormatHolder formatHolder) {
        Format format2 = this.downstreamFormat;
        boolean z = format2 == null;
        DrmInitData drmInitData = z ? null : format2.drmInitData;
        this.downstreamFormat = format;
        DrmInitData drmInitData2 = format.drmInitData;
        DrmSessionManager drmSessionManager = this.drmSessionManager;
        formatHolder.format = drmSessionManager != null ? format.copyWithCryptoType(drmSessionManager.getCryptoType(format)) : format;
        formatHolder.drmSession = this.currentDrmSession;
        if (this.drmSessionManager == null) {
            return;
        }
        if (z || !Util.areEqual(drmInitData, drmInitData2)) {
            DrmSession drmSession = this.currentDrmSession;
            DrmSession acquireSession = this.drmSessionManager.acquireSession(this.drmEventDispatcher, format);
            this.currentDrmSession = acquireSession;
            formatHolder.drmSession = acquireSession;
            if (drmSession != null) {
                drmSession.release(this.drmEventDispatcher);
            }
        }
    }

    private synchronized int peekSampleMetadata(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z, boolean z2, SampleExtrasHolder sampleExtrasHolder) {
        try {
            decoderInputBuffer.waitingForKeys = false;
            if (!hasNextSample()) {
                if (!z2 && !this.isLastSampleQueued) {
                    Format format = this.upstreamFormat;
                    if (format == null || (!z && format == this.downstreamFormat)) {
                        return -3;
                    }
                    onFormatResult((Format) Assertions.checkNotNull(format), formatHolder);
                    return -5;
                }
                decoderInputBuffer.setFlags(4);
                return -4;
            }
            Format format2 = ((SharedSampleMetadata) this.sharedSampleMetadata.get(getReadIndex())).format;
            if (!z && format2 == this.downstreamFormat) {
                int relativeIndex = getRelativeIndex(this.readPosition);
                if (!mayReadSample(relativeIndex)) {
                    decoderInputBuffer.waitingForKeys = true;
                    return -3;
                }
                decoderInputBuffer.setFlags(this.flags[relativeIndex]);
                long j = this.timesUs[relativeIndex];
                decoderInputBuffer.timeUs = j;
                if (j < this.startTimeUs) {
                    decoderInputBuffer.addFlag(Integer.MIN_VALUE);
                }
                sampleExtrasHolder.size = this.sizes[relativeIndex];
                sampleExtrasHolder.offset = this.offsets[relativeIndex];
                sampleExtrasHolder.cryptoData = this.cryptoDatas[relativeIndex];
                return -4;
            }
            onFormatResult(format2, formatHolder);
            return -5;
        } catch (Throwable th) {
            throw th;
        }
    }

    private void releaseDrmSessionReferences() {
        DrmSession drmSession = this.currentDrmSession;
        if (drmSession != null) {
            drmSession.release(this.drmEventDispatcher);
            this.currentDrmSession = null;
            this.downstreamFormat = null;
        }
    }

    private synchronized void rewind() {
        this.readPosition = 0;
        this.sampleDataQueue.rewind();
    }

    private synchronized boolean setUpstreamFormat(Format format) {
        try {
            this.upstreamFormatRequired = false;
            if (Util.areEqual(format, this.upstreamFormat)) {
                return false;
            }
            if (!this.sharedSampleMetadata.isEmpty() && ((SharedSampleMetadata) this.sharedSampleMetadata.getEndValue()).format.equals(format)) {
                format = ((SharedSampleMetadata) this.sharedSampleMetadata.getEndValue()).format;
            }
            this.upstreamFormat = format;
            Format format2 = this.upstreamFormat;
            this.upstreamAllSamplesAreSyncSamples = MimeTypes.allSamplesAreSyncSamples(format2.sampleMimeType, format2.codecs);
            this.loggedUnexpectedNonSyncSample = false;
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized long discardSampleMetadataToRead() {
        int i = this.readPosition;
        if (i == 0) {
            return -1L;
        }
        return discardSamples(i);
    }

    public final void discardTo(long j, boolean z, boolean z2) {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataTo(j, z, z2));
    }

    public final void discardToEnd() {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataToEnd());
    }

    public final void discardToRead() {
        this.sampleDataQueue.discardDownstreamTo(discardSampleMetadataToRead());
    }

    public final void discardUpstreamSamples(int i) {
        this.sampleDataQueue.discardUpstreamSampleBytes(discardUpstreamSampleMetadata(i));
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public final void format(Format format) {
        Format adjustedUpstreamFormat = getAdjustedUpstreamFormat(format);
        this.upstreamFormatAdjustmentRequired = false;
        this.unadjustedUpstreamFormat = format;
        boolean upstreamFormat = setUpstreamFormat(adjustedUpstreamFormat);
        UpstreamFormatChangedListener upstreamFormatChangedListener = this.upstreamFormatChangeListener;
        if (upstreamFormatChangedListener == null || !upstreamFormat) {
            return;
        }
        upstreamFormatChangedListener.onUpstreamFormatChanged(adjustedUpstreamFormat);
    }

    protected Format getAdjustedUpstreamFormat(Format format) {
        return (this.sampleOffsetUs == 0 || format.subsampleOffsetUs == Long.MAX_VALUE) ? format : format.buildUpon().setSubsampleOffsetUs(format.subsampleOffsetUs + this.sampleOffsetUs).build();
    }

    public final int getFirstIndex() {
        return this.absoluteFirstIndex;
    }

    public final synchronized long getFirstTimestampUs() {
        return this.length == 0 ? Long.MIN_VALUE : this.timesUs[this.relativeFirstIndex];
    }

    public final synchronized long getLargestQueuedTimestampUs() {
        return this.largestQueuedTimestampUs;
    }

    public final synchronized long getLargestReadTimestampUs() {
        return Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.readPosition));
    }

    public final int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }

    public final synchronized int getSkipCount(long j, boolean z) {
        int relativeIndex = getRelativeIndex(this.readPosition);
        if (hasNextSample() && j >= this.timesUs[relativeIndex]) {
            if (j > this.largestQueuedTimestampUs && z) {
                return this.length - this.readPosition;
            }
            int findSampleBefore = findSampleBefore(relativeIndex, this.length - this.readPosition, j, true);
            if (findSampleBefore == -1) {
                return 0;
            }
            return findSampleBefore;
        }
        return 0;
    }

    public final synchronized Format getUpstreamFormat() {
        return this.upstreamFormatRequired ? null : this.upstreamFormat;
    }

    public final int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }

    protected final void invalidateUpstreamFormatAdjustment() {
        this.upstreamFormatAdjustmentRequired = true;
    }

    public final synchronized boolean isLastSampleQueued() {
        return this.isLastSampleQueued;
    }

    public synchronized boolean isReady(boolean z) {
        Format format;
        boolean z2 = true;
        if (hasNextSample()) {
            if (((SharedSampleMetadata) this.sharedSampleMetadata.get(getReadIndex())).format != this.downstreamFormat) {
                return true;
            }
            return mayReadSample(getRelativeIndex(this.readPosition));
        }
        if (!z && !this.isLastSampleQueued && ((format = this.upstreamFormat) == null || format == this.downstreamFormat)) {
            z2 = false;
        }
        return z2;
    }

    public void maybeThrowError() {
        DrmSession drmSession = this.currentDrmSession;
        if (drmSession != null && drmSession.getState() == 1) {
            throw ((DrmSession.DrmSessionException) Assertions.checkNotNull(this.currentDrmSession.getError()));
        }
    }

    public final synchronized int peekSourceId() {
        try {
        } catch (Throwable th) {
            throw th;
        }
        return hasNextSample() ? this.sourceIds[getRelativeIndex(this.readPosition)] : this.upstreamSourceId;
    }

    public void preRelease() {
        discardToEnd();
        releaseDrmSessionReferences();
    }

    public int read(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, int i, boolean z) {
        int peekSampleMetadata = peekSampleMetadata(formatHolder, decoderInputBuffer, (i & 2) != 0, z, this.extrasHolder);
        if (peekSampleMetadata == -4 && !decoderInputBuffer.isEndOfStream()) {
            boolean z2 = (i & 1) != 0;
            if ((i & 4) == 0) {
                SampleDataQueue sampleDataQueue = this.sampleDataQueue;
                SampleExtrasHolder sampleExtrasHolder = this.extrasHolder;
                if (z2) {
                    sampleDataQueue.peekToBuffer(decoderInputBuffer, sampleExtrasHolder);
                } else {
                    sampleDataQueue.readToBuffer(decoderInputBuffer, sampleExtrasHolder);
                }
            }
            if (!z2) {
                this.readPosition++;
            }
        }
        return peekSampleMetadata;
    }

    public void release() {
        reset(true);
        releaseDrmSessionReferences();
    }

    public final void reset() {
        reset(false);
    }

    public void reset(boolean z) {
        this.sampleDataQueue.reset();
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.startTimeUs = Long.MIN_VALUE;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.isLastSampleQueued = false;
        this.sharedSampleMetadata.clear();
        if (z) {
            this.unadjustedUpstreamFormat = null;
            this.upstreamFormat = null;
            this.upstreamFormatRequired = true;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public /* synthetic */ int sampleData(DataReader dataReader, int i, boolean z) {
        int sampleData;
        sampleData = sampleData(dataReader, i, z, 0);
        return sampleData;
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public final int sampleData(DataReader dataReader, int i, boolean z, int i2) {
        return this.sampleDataQueue.sampleData(dataReader, i, z);
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public /* synthetic */ void sampleData(ParsableByteArray parsableByteArray, int i) {
        sampleData(parsableByteArray, i, 0);
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public final void sampleData(ParsableByteArray parsableByteArray, int i, int i2) {
        this.sampleDataQueue.sampleData(parsableByteArray, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0059  */
    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void sampleMetadata(long j, int i, int i2, int i3, TrackOutput.CryptoData cryptoData) {
        int i4;
        if (this.upstreamFormatAdjustmentRequired) {
            format((Format) Assertions.checkStateNotNull(this.unadjustedUpstreamFormat));
        }
        int i5 = i & 1;
        boolean z = i5 != 0;
        if (this.upstreamKeyframeRequired) {
            if (!z) {
                return;
            } else {
                this.upstreamKeyframeRequired = false;
            }
        }
        long j2 = this.sampleOffsetUs + j;
        if (this.upstreamAllSamplesAreSyncSamples) {
            if (j2 < this.startTimeUs) {
                return;
            }
            if (i5 == 0) {
                if (!this.loggedUnexpectedNonSyncSample) {
                    Log.w("SampleQueue", "Overriding unexpected non-sync sample for format: " + this.upstreamFormat);
                    this.loggedUnexpectedNonSyncSample = true;
                }
                i4 = i | 1;
                if (this.pendingSplice) {
                    if (!z || !attemptSplice(j2)) {
                        return;
                    } else {
                        this.pendingSplice = false;
                    }
                }
                commitSample(j2, i4, (this.sampleDataQueue.getTotalBytesWritten() - i2) - i3, i2, cryptoData);
            }
        }
        i4 = i;
        if (this.pendingSplice) {
        }
        commitSample(j2, i4, (this.sampleDataQueue.getTotalBytesWritten() - i2) - i3, i2, cryptoData);
    }

    public final synchronized boolean seekTo(int i) {
        rewind();
        int i2 = this.absoluteFirstIndex;
        if (i >= i2 && i <= this.length + i2) {
            this.startTimeUs = Long.MIN_VALUE;
            this.readPosition = i - i2;
            return true;
        }
        return false;
    }

    public final synchronized boolean seekTo(long j, boolean z) {
        rewind();
        int relativeIndex = getRelativeIndex(this.readPosition);
        if (hasNextSample() && j >= this.timesUs[relativeIndex] && (j <= this.largestQueuedTimestampUs || z)) {
            int findSampleBefore = findSampleBefore(relativeIndex, this.length - this.readPosition, j, true);
            if (findSampleBefore == -1) {
                return false;
            }
            this.startTimeUs = j;
            this.readPosition += findSampleBefore;
            return true;
        }
        return false;
    }

    public final void setSampleOffsetUs(long j) {
        if (this.sampleOffsetUs != j) {
            this.sampleOffsetUs = j;
            invalidateUpstreamFormatAdjustment();
        }
    }

    public final void setStartTimeUs(long j) {
        this.startTimeUs = j;
    }

    public final void setUpstreamFormatChangeListener(UpstreamFormatChangedListener upstreamFormatChangedListener) {
        this.upstreamFormatChangeListener = upstreamFormatChangedListener;
    }

    public final synchronized void skip(int i) {
        boolean z;
        if (i >= 0) {
            try {
                if (this.readPosition + i <= this.length) {
                    z = true;
                    Assertions.checkArgument(z);
                    this.readPosition += i;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        z = false;
        Assertions.checkArgument(z);
        this.readPosition += i;
    }

    public final void sourceId(int i) {
        this.upstreamSourceId = i;
    }

    public final void splice() {
        this.pendingSplice = true;
    }
}
