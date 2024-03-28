package j$.util.stream;

import j$.util.t;
/* loaded from: classes2.dex */
abstract class C3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static j$.util.t b(f4 f4Var, j$.util.t tVar, long j, long j2) {
        long d = d(j, j2);
        int i = A3.a[f4Var.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new v4((t.a) tVar, j, d);
                    }
                    throw new IllegalStateException("Unknown shape " + f4Var);
                }
                return new z4((t.c) tVar, j, d);
            }
            return new x4((t.b) tVar, j, d);
        }
        return new D4(tVar, j, d);
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
        return (j != -1 ? e4.u : 0) | e4.t;
    }

    public static V f(c cVar, long j, long j2) {
        if (j >= 0) {
            return new z3(cVar, f4.DOUBLE_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static IntStream g(c cVar, long j, long j2) {
        if (j >= 0) {
            return new t3(cVar, f4.INT_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static f1 h(c cVar, long j, long j2) {
        if (j >= 0) {
            return new w3(cVar, f4.LONG_VALUE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static Stream i(c cVar, long j, long j2) {
        if (j >= 0) {
            return new q3(cVar, f4.REFERENCE, e(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }
}
