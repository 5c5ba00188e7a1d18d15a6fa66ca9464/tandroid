package j$.util.stream;

import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class y2 {
    private static final B1 a = new a2(null);
    private static final x1 b = new Y1();
    private static final z1 c = new Z1();
    private static final v1 d = new X1();
    private static final int[] e = new int[0];
    private static final long[] f = new long[0];
    private static final double[] g = new double[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public static t1 d(long j, j$.util.function.m mVar) {
        return (j < 0 || j >= 2147483639) ? new u2() : new c2(j, mVar);
    }

    public static B1 e(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        long o0 = z2Var.o0(tVar);
        if (o0 < 0 || !tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            B1 b1 = (B1) new I1(z2Var, mVar, tVar).invoke();
            return z ? l(b1, mVar) : b1;
        } else if (o0 < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) o0);
            new s2(tVar, z2Var, objArr).invoke();
            return new E1(objArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static v1 f(z2 z2Var, j$.util.t tVar, boolean z) {
        long o0 = z2Var.o0(tVar);
        if (o0 < 0 || !tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            v1 v1Var = (v1) new I1(z2Var, tVar, 0).invoke();
            return z ? m(v1Var) : v1Var;
        } else if (o0 < 2147483639) {
            double[] dArr = new double[(int) o0];
            new p2(tVar, z2Var, dArr).invoke();
            return new U1(dArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static x1 g(z2 z2Var, j$.util.t tVar, boolean z) {
        long o0 = z2Var.o0(tVar);
        if (o0 < 0 || !tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            x1 x1Var = (x1) new I1(z2Var, tVar, 1).invoke();
            return z ? n(x1Var) : x1Var;
        } else if (o0 < 2147483639) {
            int[] iArr = new int[(int) o0];
            new q2(tVar, z2Var, iArr).invoke();
            return new d2(iArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static z1 h(z2 z2Var, j$.util.t tVar, boolean z) {
        long o0 = z2Var.o0(tVar);
        if (o0 < 0 || !tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            z1 z1Var = (z1) new I1(z2Var, tVar, 2).invoke();
            return z ? o(z1Var) : z1Var;
        } else if (o0 < 2147483639) {
            long[] jArr = new long[(int) o0];
            new r2(tVar, z2Var, jArr).invoke();
            return new m2(jArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static B1 i(f4 f4Var, B1 b1, B1 b12) {
        int i = C1.a[f4Var.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new P1((v1) b1, (v1) b12);
                    }
                    throw new IllegalStateException("Unknown shape " + f4Var);
                }
                return new R1((z1) b1, (z1) b12);
            }
            return new Q1((x1) b1, (x1) b12);
        }
        return new T1(b1, b12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static q1 j(long j) {
        return (j < 0 || j >= 2147483639) ? new W1() : new V1(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static B1 k(f4 f4Var) {
        int i = C1.a[f4Var.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return d;
                    }
                    throw new IllegalStateException("Unknown shape " + f4Var);
                }
                return c;
            }
            return b;
        }
        return a;
    }

    public static B1 l(B1 b1, j$.util.function.m mVar) {
        if (b1.p() > 0) {
            long count = b1.count();
            if (count < 2147483639) {
                Object[] objArr = (Object[]) mVar.apply((int) count);
                new w2(b1, objArr, 0, (C1) null).invoke();
                return new E1(objArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return b1;
    }

    public static v1 m(v1 v1Var) {
        if (v1Var.p() > 0) {
            long count = v1Var.count();
            if (count < 2147483639) {
                double[] dArr = new double[(int) count];
                new v2(v1Var, dArr, 0).invoke();
                return new U1(dArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return v1Var;
    }

    public static x1 n(x1 x1Var) {
        if (x1Var.p() > 0) {
            long count = x1Var.count();
            if (count < 2147483639) {
                int[] iArr = new int[(int) count];
                new v2(x1Var, iArr, 0).invoke();
                return new d2(iArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return x1Var;
    }

    public static z1 o(z1 z1Var) {
        if (z1Var.p() > 0) {
            long count = z1Var.count();
            if (count < 2147483639) {
                long[] jArr = new long[(int) count];
                new v2(z1Var, jArr, 0).invoke();
                return new m2(jArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return z1Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static r1 p(long j) {
        return (j < 0 || j >= 2147483639) ? new f2() : new e2(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static s1 q(long j) {
        return (j < 0 || j >= 2147483639) ? new o2() : new n2(j);
    }
}
