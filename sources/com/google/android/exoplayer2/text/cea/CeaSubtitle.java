package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class CeaSubtitle implements Subtitle {
    private final List cues;

    public CeaSubtitle(List list) {
        this.cues = list;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List getCues(long j) {
        return j >= 0 ? this.cues : Collections.emptyList();
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int i) {
        Assertions.checkArgument(i == 0);
        return 0L;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long j) {
        return j < 0 ? 0 : -1;
    }
}
