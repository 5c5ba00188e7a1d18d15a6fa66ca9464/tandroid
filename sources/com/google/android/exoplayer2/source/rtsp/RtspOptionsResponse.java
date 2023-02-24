package com.google.android.exoplayer2.source.rtsp;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
final class RtspOptionsResponse {
    public final ImmutableList<Integer> supportedMethods;

    public RtspOptionsResponse(int i, List<Integer> list) {
        this.supportedMethods = ImmutableList.copyOf((Collection) list);
    }
}
