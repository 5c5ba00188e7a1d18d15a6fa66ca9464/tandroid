package j$.util.stream;
/* loaded from: classes2.dex */
public abstract class x2 {
    private static final A1 a = new Z1(null);
    private static final w1 b = new X1();
    private static final y1 c = new Y1();
    private static final u1 d = new W1();
    private static final int[] e = new int[0];
    private static final long[] f = new long[0];
    private static final double[] g = new double[0];

    public static s1 d(long j, j$.util.function.m mVar) {
        return (j < 0 || j >= 2147483639) ? new t2() : new b2(j, mVar);
    }

    public static A1 e(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        long q0 = y2Var.q0(uVar);
        if (q0 < 0 || !uVar.hasCharacteristics(16384)) {
            A1 a1 = (A1) new H1(y2Var, mVar, uVar).invoke();
            return z ? l(a1, mVar) : a1;
        } else if (q0 < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) q0);
            new r2(uVar, y2Var, objArr).invoke();
            return new D1(objArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static u1 f(y2 y2Var, j$.util.u uVar, boolean z) {
        long q0 = y2Var.q0(uVar);
        if (q0 < 0 || !uVar.hasCharacteristics(16384)) {
            u1 u1Var = (u1) new H1(y2Var, uVar, 0).invoke();
            return z ? m(u1Var) : u1Var;
        } else if (q0 < 2147483639) {
            double[] dArr = new double[(int) q0];
            new o2(uVar, y2Var, dArr).invoke();
            return new T1(dArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static w1 g(y2 y2Var, j$.util.u uVar, boolean z) {
        long q0 = y2Var.q0(uVar);
        if (q0 < 0 || !uVar.hasCharacteristics(16384)) {
            w1 w1Var = (w1) new H1(y2Var, uVar, 1).invoke();
            return z ? n(w1Var) : w1Var;
        } else if (q0 < 2147483639) {
            int[] iArr = new int[(int) q0];
            new p2(uVar, y2Var, iArr).invoke();
            return new c2(iArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static y1 h(y2 y2Var, j$.util.u uVar, boolean z) {
        long q0 = y2Var.q0(uVar);
        if (q0 < 0 || !uVar.hasCharacteristics(16384)) {
            y1 y1Var = (y1) new H1(y2Var, uVar, 2).invoke();
            return z ? o(y1Var) : y1Var;
        } else if (q0 < 2147483639) {
            long[] jArr = new long[(int) q0];
            new q2(uVar, y2Var, jArr).invoke();
            return new l2(jArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static A1 i(e4 e4Var, A1 a1, A1 a12) {
        int i = B1.a[e4Var.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return new P1((w1) a1, (w1) a12);
            }
            if (i == 3) {
                return new Q1((y1) a1, (y1) a12);
            }
            if (i == 4) {
                return new O1((u1) a1, (u1) a12);
            }
            throw new IllegalStateException("Unknown shape " + e4Var);
        }
        return new S1(a1, a12);
    }

    public static p1 j(long j) {
        return (j < 0 || j >= 2147483639) ? new V1() : new U1(j);
    }

    public static A1 k(e4 e4Var) {
        int i = B1.a[e4Var.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return b;
            }
            if (i == 3) {
                return c;
            }
            if (i == 4) {
                return d;
            }
            throw new IllegalStateException("Unknown shape " + e4Var);
        }
        return a;
    }

    public static A1 l(A1 a1, j$.util.function.m mVar) {
        if (a1.p() > 0) {
            long count = a1.count();
            if (count < 2147483639) {
                Object[] objArr = (Object[]) mVar.apply((int) count);
                new v2(a1, objArr, 0, (B1) null).invoke();
                return new D1(objArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return a1;
    }

    public static u1 m(u1 u1Var) {
        if (u1Var.p() > 0) {
            long count = u1Var.count();
            if (count < 2147483639) {
                double[] dArr = new double[(int) count];
                new u2(u1Var, dArr, 0).invoke();
                return new T1(dArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return u1Var;
    }

    public static w1 n(w1 w1Var) {
        if (w1Var.p() > 0) {
            long count = w1Var.count();
            if (count < 2147483639) {
                int[] iArr = new int[(int) count];
                new u2(w1Var, iArr, 0).invoke();
                return new c2(iArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return w1Var;
    }

    public static y1 o(y1 y1Var) {
        if (y1Var.p() > 0) {
            long count = y1Var.count();
            if (count < 2147483639) {
                long[] jArr = new long[(int) count];
                new u2(y1Var, jArr, 0).invoke();
                return new l2(jArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return y1Var;
    }

    public static q1 p(long j) {
        return (j < 0 || j >= 2147483639) ? new e2() : new d2(j);
    }

    public static r1 q(long j) {
        return (j < 0 || j >= 2147483639) ? new n2() : new m2(j);
    }
}
