package j$.time;

import j$.util.function.BiConsumer;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static void a(ConcurrentMap concurrentMap, BiConsumer biConsumer) {
        biConsumer.getClass();
        for (Map.Entry entry : concurrentMap.entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException unused) {
            }
        }
    }

    public static /* synthetic */ void b(j$.util.function.m mVar, j$.util.function.m mVar2, double d) {
        mVar.accept(d);
        mVar2.accept(d);
    }

    public static /* synthetic */ long c(long j, long j2) {
        long j3 = j + j2;
        if (((j2 ^ j) < 0) || ((j ^ j3) >= 0)) {
            return j3;
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ long d(long j, long j2) {
        long j3 = j % j2;
        if (j3 == 0) {
            return 0L;
        }
        return (((j ^ j2) >> 63) | 1) > 0 ? j3 : j3 + j2;
    }

    public static /* synthetic */ long e(long j) {
        int numberOfLeadingZeros = Long.numberOfLeadingZeros(-1001L) + Long.numberOfLeadingZeros(1000L) + Long.numberOfLeadingZeros((-1) ^ j) + Long.numberOfLeadingZeros(j);
        if (numberOfLeadingZeros > 65) {
            return j * 1000;
        }
        if (numberOfLeadingZeros >= 64) {
            if (true | (j >= 0)) {
                long j2 = j * 1000;
                if (j == 0 || j2 / j == 1000) {
                    return j2;
                }
            }
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ long f(long j, long j2) {
        long j3 = j / j2;
        return (j - (j2 * j3) != 0 && (((j ^ j2) >> 63) | 1) < 0) ? j3 - 1 : j3;
    }
}
