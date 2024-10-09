package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.SeekMap;

/* loaded from: classes.dex */
public abstract class ForwardingSeekMap implements SeekMap {
    private final SeekMap seekMap;

    public ForwardingSeekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long j) {
        return this.seekMap.getSeekPoints(j);
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return this.seekMap.isSeekable();
    }
}
