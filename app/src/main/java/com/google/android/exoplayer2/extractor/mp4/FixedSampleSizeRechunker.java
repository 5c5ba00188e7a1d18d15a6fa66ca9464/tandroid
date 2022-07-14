package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Util;
/* loaded from: classes3.dex */
public final class FixedSampleSizeRechunker {
    private static final int MAX_SAMPLE_SIZE = 8192;

    /* loaded from: classes3.dex */
    public static final class Results {
        public final long duration;
        public final int[] flags;
        public final int maximumSize;
        public final long[] offsets;
        public final int[] sizes;
        public final long[] timestamps;

        private Results(long[] offsets, int[] sizes, int maximumSize, long[] timestamps, int[] flags, long duration) {
            this.offsets = offsets;
            this.sizes = sizes;
            this.maximumSize = maximumSize;
            this.timestamps = timestamps;
            this.flags = flags;
            this.duration = duration;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:7:0x0027 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Results rechunk(int fixedSampleSize, long[] chunkOffsets, int[] chunkSampleCounts, long timestampDeltaInTimeUnits) {
        int maxSampleCount = 8192 / fixedSampleSize;
        int rechunkedSampleCount = 0;
        for (int chunkSampleCount : chunkSampleCounts) {
            rechunkedSampleCount += Util.ceilDivide(chunkSampleCount, maxSampleCount);
        }
        long[] offsets = new long[rechunkedSampleCount];
        int[] sizes = new int[rechunkedSampleCount];
        long[] timestamps = new long[rechunkedSampleCount];
        int[] flags = new int[rechunkedSampleCount];
        int chunkIndex = 0;
        int maximumSize = 0;
        int originalSampleIndex = 0;
        int newSampleIndex = 0;
        while (chunkIndex < maximumSize) {
            int chunkSamplesRemaining = chunkSampleCounts[chunkIndex];
            long sampleOffset = chunkOffsets[chunkIndex];
            int maximumSize2 = maximumSize;
            while (chunkSamplesRemaining > 0) {
                int bufferSampleCount = Math.min(maxSampleCount, chunkSamplesRemaining);
                offsets[newSampleIndex] = sampleOffset;
                sizes[newSampleIndex] = fixedSampleSize * bufferSampleCount;
                maximumSize2 = Math.max(maximumSize2, sizes[newSampleIndex]);
                timestamps[newSampleIndex] = originalSampleIndex * timestampDeltaInTimeUnits;
                flags[newSampleIndex] = 1;
                sampleOffset += sizes[newSampleIndex];
                originalSampleIndex += bufferSampleCount;
                chunkSamplesRemaining -= bufferSampleCount;
                newSampleIndex++;
            }
            chunkIndex++;
            maximumSize = maximumSize2;
        }
        long duration = timestampDeltaInTimeUnits * originalSampleIndex;
        return new Results(offsets, sizes, maximumSize, timestamps, flags, duration);
    }

    private FixedSampleSizeRechunker() {
    }
}
