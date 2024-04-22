package j$.util.stream;

import j$.util.s;
/* loaded from: classes2.dex */
abstract class B3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static j$.util.s b(e4 e4Var, j$.util.s sVar, long j, long j2) {
        long d = d(j, j2);
        int i = z3.a[e4Var.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new u4((s.a) sVar, j, d);
                    }
                    throw new IllegalStateException("Unknown shape " + e4Var);
                }
                return new y4((s.c) sVar, j, d);
            }
            return new w4((s.b) sVar, j, d);
        }
        return new C4(sVar, j, d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long c(long j, long j2, long j3) {
        if (j >= 0) {
            return Math.max(-1L, Math.min(j - j2, j3));
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long d(long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        if (j3 >= 0) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    private static int e(long j) {
        return (j != -1 ? d4.u : 0) | d4.t;
    }

    public static V f(c cVar, long j, long j2) {
        if (j >= 0) {
            return new y3(cVar, e4.DOUBLE_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static IntStream g(c cVar, long j, long j2) {
        if (j >= 0) {
            return new s3(cVar, e4.INT_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static LongStream h(c cVar, long j, long j2) {
        if (j >= 0) {
            return new v3(cVar, e4.LONG_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static Stream i(c cVar, long j, long j2) {
        if (j >= 0) {
            return new p3(cVar, e4.REFERENCE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }
}
