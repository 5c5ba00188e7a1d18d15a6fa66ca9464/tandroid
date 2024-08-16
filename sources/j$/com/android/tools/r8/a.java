package j$.com.android.tools.r8;

import j$.util.concurrent.s;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.C;
import j$.util.function.F;
import j$.util.function.Function;
import j$.util.function.T;
import j$.util.function.W;
import j$.util.function.k;
import j$.util.function.n;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import sun.misc.Unsafe;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static s a(BiFunction biFunction, Function function) {
        function.getClass();
        return new s(biFunction, function);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.k] */
    public static k b(final n nVar, final n nVar2) {
        nVar2.getClass();
        return new n() { // from class: j$.util.function.k
            @Override // j$.util.function.n
            public final void accept(double d) {
                n.this.accept(d);
                nVar2.accept(d);
            }

            @Override // j$.util.function.n
            public final /* synthetic */ n k(n nVar3) {
                return j$.com.android.tools.r8.a.b(this, nVar3);
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.C] */
    public static C c(final F f, final F f2) {
        f2.getClass();
        return new F() { // from class: j$.util.function.C
            @Override // j$.util.function.F
            public final void accept(int i) {
                F.this.accept(i);
                f2.accept(i);
            }

            @Override // j$.util.function.F
            public final /* synthetic */ F l(F f3) {
                return j$.com.android.tools.r8.a.c(this, f3);
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.T] */
    public static T d(final W w, final W w2) {
        w2.getClass();
        return new W() { // from class: j$.util.function.T
            @Override // j$.util.function.W
            public final void accept(long j) {
                W.this.accept(j);
                w2.accept(j);
            }

            @Override // j$.util.function.W
            public final /* synthetic */ W f(W w3) {
                return j$.com.android.tools.r8.a.d(this, w3);
            }
        };
    }

    public static void e(ConcurrentMap concurrentMap, BiConsumer biConsumer) {
        biConsumer.getClass();
        for (Map.Entry entry : concurrentMap.entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException unused) {
            }
        }
    }

    public static /* synthetic */ long f(long j) {
        int numberOfLeadingZeros = Long.numberOfLeadingZeros(-1001L) + Long.numberOfLeadingZeros(1000L) + Long.numberOfLeadingZeros((-1) ^ j) + Long.numberOfLeadingZeros(j);
        if (numberOfLeadingZeros > 65) {
            return j * 1000;
        }
        if (numberOfLeadingZeros >= 64) {
            long j2 = j * 1000;
            if (j == 0 || j2 / j == 1000) {
                return j2;
            }
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ long g(long j, long j2) {
        long j3 = j + j2;
        if (((j2 ^ j) < 0) || ((j ^ j3) >= 0)) {
            return j3;
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ boolean h(Unsafe unsafe, Object obj, long j, Object obj2) {
        while (!unsafe.compareAndSwapObject(obj, j, (Object) null, obj2)) {
            if (unsafe.getObject(obj, j) != null) {
                return false;
            }
        }
        return true;
    }

    public static /* synthetic */ long i(long j, long j2) {
        long j3 = j % j2;
        if (j3 == 0) {
            return 0L;
        }
        return (((j ^ j2) >> 63) | 1) > 0 ? j3 : j3 + j2;
    }

    public static /* synthetic */ long j(long j, long j2) {
        long j3 = j / j2;
        return (j - (j2 * j3) != 0 && (((j ^ j2) >> 63) | 1) < 0) ? j3 - 1 : j3;
    }
}
