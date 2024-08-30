package com.google.android.exoplayer2.mediacodec;

import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public abstract /* synthetic */ class AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding0 {
    public static /* synthetic */ boolean m(AtomicReference atomicReference, Object obj, Object obj2) {
        while (!atomicReference.compareAndSet(obj, obj2)) {
            if (atomicReference.get() != obj) {
                return false;
            }
        }
        return true;
    }
}
