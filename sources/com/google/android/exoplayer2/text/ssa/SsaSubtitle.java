package com.google.android.exoplayer2.text.ssa;

import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
final class SsaSubtitle implements Subtitle {
    private final List cueTimesUs;
    private final List cues;

    public SsaSubtitle(List list, List list2) {
        this.cues = list;
        this.cueTimesUs = list2;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List getCues(long j) {
        int binarySearchFloor = Util.binarySearchFloor(this.cueTimesUs, (Comparable) Long.valueOf(j), true, false);
        return binarySearchFloor == -1 ? Collections.emptyList() : (List) this.cues.get(binarySearchFloor);
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int i) {
        Assertions.checkArgument(i >= 0);
        Assertions.checkArgument(i < this.cueTimesUs.size());
        return ((Long) this.cueTimesUs.get(i)).longValue();
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return this.cueTimesUs.size();
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long j) {
        int binarySearchCeil = Util.binarySearchCeil(this.cueTimesUs, (Comparable) Long.valueOf(j), false, false);
        if (binarySearchCeil < this.cueTimesUs.size()) {
            return binarySearchCeil;
        }
        return -1;
    }
}
