package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public abstract class SegmentOrClosed {
    public static Object constructor-impl(Object obj) {
        return obj;
    }

    public static final Segment getSegment-impl(Object obj) {
        if (obj == ConcurrentLinkedListKt.CLOSED) {
            throw new IllegalStateException("Does not contain segment".toString());
        }
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type S of kotlinx.coroutines.internal.SegmentOrClosed");
        return (Segment) obj;
    }

    public static final boolean isClosed-impl(Object obj) {
        return obj == ConcurrentLinkedListKt.CLOSED;
    }
}
