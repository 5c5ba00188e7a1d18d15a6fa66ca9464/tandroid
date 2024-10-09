package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.SlidingPercentile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* loaded from: classes.dex */
public class SlidingPercentile {
    private static final Comparator INDEX_COMPARATOR = new Comparator() { // from class: com.google.android.exoplayer2.upstream.SlidingPercentile$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int lambda$static$0;
            lambda$static$0 = SlidingPercentile.lambda$static$0((SlidingPercentile.Sample) obj, (SlidingPercentile.Sample) obj2);
            return lambda$static$0;
        }
    };
    private static final Comparator VALUE_COMPARATOR = new Comparator() { // from class: com.google.android.exoplayer2.upstream.SlidingPercentile$$ExternalSyntheticLambda1
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int lambda$static$1;
            lambda$static$1 = SlidingPercentile.lambda$static$1((SlidingPercentile.Sample) obj, (SlidingPercentile.Sample) obj2);
            return lambda$static$1;
        }
    };
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private int totalWeight;
    private final Sample[] recycledSamples = new Sample[5];
    private final ArrayList samples = new ArrayList();
    private int currentSortOrder = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Sample {
        public int index;
        public float value;
        public int weight;

        private Sample() {
        }
    }

    public SlidingPercentile(int i) {
        this.maxWeight = i;
    }

    private void ensureSortedByIndex() {
        if (this.currentSortOrder != 1) {
            Collections.sort(this.samples, INDEX_COMPARATOR);
            this.currentSortOrder = 1;
        }
    }

    private void ensureSortedByValue() {
        if (this.currentSortOrder != 0) {
            Collections.sort(this.samples, VALUE_COMPARATOR);
            this.currentSortOrder = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(Sample sample, Sample sample2) {
        return sample.index - sample2.index;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$1(Sample sample, Sample sample2) {
        return Float.compare(sample.value, sample2.value);
    }

    public void addSample(int i, float f) {
        Sample sample;
        int i2;
        Sample sample2;
        int i3;
        ensureSortedByIndex();
        int i4 = this.recycledSampleCount;
        if (i4 > 0) {
            Sample[] sampleArr = this.recycledSamples;
            int i5 = i4 - 1;
            this.recycledSampleCount = i5;
            sample = sampleArr[i5];
        } else {
            sample = new Sample();
        }
        int i6 = this.nextSampleIndex;
        this.nextSampleIndex = i6 + 1;
        sample.index = i6;
        sample.weight = i;
        sample.value = f;
        this.samples.add(sample);
        int i7 = this.totalWeight + i;
        while (true) {
            this.totalWeight = i7;
            while (true) {
                int i8 = this.totalWeight;
                int i9 = this.maxWeight;
                if (i8 <= i9) {
                    return;
                }
                i2 = i8 - i9;
                sample2 = (Sample) this.samples.get(0);
                i3 = sample2.weight;
                if (i3 <= i2) {
                    this.totalWeight -= i3;
                    this.samples.remove(0);
                    int i10 = this.recycledSampleCount;
                    if (i10 < 5) {
                        Sample[] sampleArr2 = this.recycledSamples;
                        this.recycledSampleCount = i10 + 1;
                        sampleArr2[i10] = sample2;
                    }
                }
            }
            sample2.weight = i3 - i2;
            i7 = this.totalWeight - i2;
        }
    }

    public float getPercentile(float f) {
        ensureSortedByValue();
        float f2 = f * this.totalWeight;
        int i = 0;
        for (int i2 = 0; i2 < this.samples.size(); i2++) {
            Sample sample = (Sample) this.samples.get(i2);
            i += sample.weight;
            if (i >= f2) {
                return sample.value;
            }
        }
        if (this.samples.isEmpty()) {
            return Float.NaN;
        }
        return ((Sample) this.samples.get(r5.size() - 1)).value;
    }

    public void reset() {
        this.samples.clear();
        this.currentSortOrder = -1;
        this.nextSampleIndex = 0;
        this.totalWeight = 0;
    }
}
