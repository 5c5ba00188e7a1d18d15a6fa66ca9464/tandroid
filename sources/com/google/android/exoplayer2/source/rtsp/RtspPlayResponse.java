package com.google.android.exoplayer2.source.rtsp;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
final class RtspPlayResponse {
    public final RtspSessionTiming sessionTiming;
    public final ImmutableList<RtspTrackTiming> trackTimingList;

    public RtspPlayResponse(int i, RtspSessionTiming rtspSessionTiming, List<RtspTrackTiming> list) {
        this.sessionTiming = rtspSessionTiming;
        this.trackTimingList = ImmutableList.copyOf((Collection) list);
    }
}
