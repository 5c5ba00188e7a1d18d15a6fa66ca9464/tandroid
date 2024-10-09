package com.google.android.exoplayer2.drm;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes.dex */
public final class MediaDrmCallbackException extends IOException {
    public final long bytesLoaded;
    public final DataSpec dataSpec;
    public final Map responseHeaders;
    public final Uri uriAfterRedirects;

    public MediaDrmCallbackException(DataSpec dataSpec, Uri uri, Map map, long j, Throwable th) {
        super(th);
        this.dataSpec = dataSpec;
        this.uriAfterRedirects = uri;
        this.responseHeaders = map;
        this.bytesLoaded = j;
    }
}
