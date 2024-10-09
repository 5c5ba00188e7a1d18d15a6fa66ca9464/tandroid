package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

/* loaded from: classes.dex */
public interface DashSegmentIndex {
    long getAvailableSegmentCount(long j, long j2);

    long getDurationUs(long j, long j2);

    long getFirstAvailableSegmentNum(long j, long j2);

    long getFirstSegmentNum();

    long getNextSegmentAvailableTimeUs(long j, long j2);

    long getSegmentCount(long j);

    long getSegmentNum(long j, long j2);

    RangedUri getSegmentUrl(long j);

    long getTimeUs(long j);

    boolean isExplicit();
}
