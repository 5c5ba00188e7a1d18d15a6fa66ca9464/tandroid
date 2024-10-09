package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
final class FullSegmentEncryptionKeyCache {
    private final LinkedHashMap backingMap;

    public FullSegmentEncryptionKeyCache(final int i) {
        this.backingMap = new LinkedHashMap(i + 1, 1.0f, false) { // from class: com.google.android.exoplayer2.source.hls.FullSegmentEncryptionKeyCache.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry entry) {
                return size() > i;
            }
        };
    }

    public byte[] get(Uri uri) {
        if (uri == null) {
            return null;
        }
        return (byte[]) this.backingMap.get(uri);
    }

    public byte[] put(Uri uri, byte[] bArr) {
        return (byte[]) this.backingMap.put((Uri) Assertions.checkNotNull(uri), (byte[]) Assertions.checkNotNull(bArr));
    }

    public byte[] remove(Uri uri) {
        return (byte[]) this.backingMap.remove(Assertions.checkNotNull(uri));
    }
}
