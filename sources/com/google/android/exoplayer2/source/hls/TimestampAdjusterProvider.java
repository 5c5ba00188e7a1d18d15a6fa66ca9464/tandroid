package com.google.android.exoplayer2.source.hls;

import android.util.SparseArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
/* loaded from: classes.dex */
public final class TimestampAdjusterProvider {
    private final SparseArray timestampAdjusters = new SparseArray();

    public TimestampAdjuster getAdjuster(int i) {
        TimestampAdjuster timestampAdjuster = (TimestampAdjuster) this.timestampAdjusters.get(i);
        if (timestampAdjuster == null) {
            TimestampAdjuster timestampAdjuster2 = new TimestampAdjuster(9223372036854775806L);
            this.timestampAdjusters.put(i, timestampAdjuster2);
            return timestampAdjuster2;
        }
        return timestampAdjuster;
    }

    public void reset() {
        this.timestampAdjusters.clear();
    }
}
