package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class DataSpec {
    public final long absoluteStreamPosition;
    public final Object customData;
    public final int flags;
    public final byte[] httpBody;
    public final int httpMethod;
    public final Map httpRequestHeaders;
    public final String key;
    public final long length;
    public final long position;
    public Uri uri;
    public final long uriPositionOffset;

    /* loaded from: classes.dex */
    public static final class Builder {
        private Object customData;
        private int flags;
        private byte[] httpBody;
        private int httpMethod;
        private Map httpRequestHeaders;
        private String key;
        private long length;
        private long position;
        private Uri uri;
        private long uriPositionOffset;

        public Builder() {
            this.httpMethod = 1;
            this.httpRequestHeaders = Collections.emptyMap();
            this.length = -1L;
        }

        private Builder(DataSpec dataSpec) {
            this.uri = dataSpec.uri;
            this.uriPositionOffset = dataSpec.uriPositionOffset;
            this.httpMethod = dataSpec.httpMethod;
            this.httpBody = dataSpec.httpBody;
            this.httpRequestHeaders = dataSpec.httpRequestHeaders;
            this.position = dataSpec.position;
            this.length = dataSpec.length;
            this.key = dataSpec.key;
            this.flags = dataSpec.flags;
            this.customData = dataSpec.customData;
        }

        public DataSpec build() {
            Assertions.checkStateNotNull(this.uri, "The uri must be set.");
            return new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, this.httpRequestHeaders, this.position, this.length, this.key, this.flags, this.customData);
        }

        public Builder setFlags(int i) {
            this.flags = i;
            return this;
        }

        public Builder setHttpBody(byte[] bArr) {
            this.httpBody = bArr;
            return this;
        }

        public Builder setHttpMethod(int i) {
            this.httpMethod = i;
            return this;
        }

        public Builder setHttpRequestHeaders(Map map) {
            this.httpRequestHeaders = map;
            return this;
        }

        public Builder setKey(String str) {
            this.key = str;
            return this;
        }

        public Builder setLength(long j) {
            this.length = j;
            return this;
        }

        public Builder setPosition(long j) {
            this.position = j;
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder setUri(String str) {
            this.uri = Uri.parse(str);
            return this;
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.datasource");
    }

    public DataSpec(Uri uri) {
        this(uri, 0L, -1L);
    }

    private DataSpec(Uri uri, long j, int i, byte[] bArr, Map map, long j2, long j3, String str, int i2, Object obj) {
        byte[] bArr2 = bArr;
        long j4 = j + j2;
        Assertions.checkArgument(j4 >= 0);
        Assertions.checkArgument(j2 >= 0);
        Assertions.checkArgument(j3 > 0 || j3 == -1);
        this.uri = uri;
        this.uriPositionOffset = j;
        this.httpMethod = i;
        this.httpBody = (bArr2 == null || bArr2.length == 0) ? null : bArr2;
        this.httpRequestHeaders = Collections.unmodifiableMap(new HashMap(map));
        this.position = j2;
        this.absoluteStreamPosition = j4;
        this.length = j3;
        this.key = str;
        this.flags = i2;
        this.customData = obj;
    }

    public DataSpec(Uri uri, long j, long j2) {
        this(uri, 0L, 1, null, Collections.emptyMap(), j, j2, null, 0, null);
    }

    public static String getStringForHttpMethod(int i) {
        if (i == 1) {
            return "GET";
        }
        if (i == 2) {
            return "POST";
        }
        if (i == 3) {
            return "HEAD";
        }
        throw new IllegalStateException();
    }

    public Builder buildUpon() {
        return new Builder();
    }

    public final String getHttpMethodString() {
        return getStringForHttpMethod(this.httpMethod);
    }

    public boolean isFlagSet(int i) {
        return (this.flags & i) == i;
    }

    public DataSpec subrange(long j) {
        long j2 = this.length;
        return subrange(j, j2 != -1 ? j2 - j : -1L);
    }

    public DataSpec subrange(long j, long j2) {
        return (j == 0 && this.length == j2) ? this : new DataSpec(this.uri, this.uriPositionOffset, this.httpMethod, this.httpBody, this.httpRequestHeaders, this.position + j, j2, this.key, this.flags, this.customData);
    }

    public String toString() {
        return "DataSpec[" + getHttpMethodString() + " " + this.uri + ", " + this.position + ", " + this.length + ", " + this.key + ", " + this.flags + "]";
    }
}
