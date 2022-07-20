package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class FullSegmentEncryptionKeyCache {
    private final LinkedHashMap<Uri, byte[]> backingMap;

    public FullSegmentEncryptionKeyCache(int i) {
        this.backingMap = new AnonymousClass1(this, i + 1, 1.0f, false, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.exoplayer2.source.hls.FullSegmentEncryptionKeyCache$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends LinkedHashMap<Uri, byte[]> {
        final /* synthetic */ int val$maxSize;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(FullSegmentEncryptionKeyCache fullSegmentEncryptionKeyCache, int i, float f, boolean z, int i2) {
            super(i, f, z);
            this.val$maxSize = i2;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Uri, byte[]> entry) {
            return size() > this.val$maxSize;
        }
    }

    public byte[] get(Uri uri) {
        if (uri == null) {
            return null;
        }
        return this.backingMap.get(uri);
    }

    public byte[] put(Uri uri, byte[] bArr) {
        return this.backingMap.put((Uri) Assertions.checkNotNull(uri), (byte[]) Assertions.checkNotNull(bArr));
    }

    public byte[] remove(Uri uri) {
        return this.backingMap.remove(Assertions.checkNotNull(uri));
    }
}
