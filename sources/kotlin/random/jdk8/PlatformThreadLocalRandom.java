package kotlin.random.jdk8;

import j$.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.AbstractPlatformRandom;

/* loaded from: classes.dex */
public final class PlatformThreadLocalRandom extends AbstractPlatformRandom {
    @Override // kotlin.random.AbstractPlatformRandom
    public Random getImpl() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        Intrinsics.checkNotNullExpressionValue(current, "current()");
        return current;
    }
}
