package com.google.android.exoplayer2.source;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes.dex */
public final class LoadEventInfo {
    private static final AtomicLong idSource = new AtomicLong();

    public static long getNewId() {
        return idSource.getAndIncrement();
    }

    public LoadEventInfo(long j, DataSpec dataSpec, long j2) {
        this(j, dataSpec, dataSpec.uri, Collections.emptyMap(), j2, 0L, 0L);
    }

    public LoadEventInfo(long j, DataSpec dataSpec, Uri uri, Map<String, List<String>> map, long j2, long j3, long j4) {
    }
}
