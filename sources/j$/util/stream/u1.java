package j$.util.stream;

import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class u1 implements C3 {
    private static final W0 a = new W0();
    private static final A0 b = new U0();
    private static final B0 c = new V0();
    private static final z0 d = new T0();
    private static final int[] e = new int[0];
    private static final long[] f = new long[0];
    private static final double[] g = new double[0];

    public /* synthetic */ u1(U2 u2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static y0 g(long j, j$.util.function.N n) {
        return (j < 0 || j >= 2147483639) ? new q1() : new Y0(j, n);
    }

    public static D0 h(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n) {
        long I0 = u0Var.I0(q);
        if (I0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            D0 d0 = (D0) new I0(q, n, u0Var).invoke();
            return z ? o(d0, n) : d0;
        } else if (I0 < 2147483639) {
            Object[] objArr = (Object[]) n.apply((int) I0);
            new o1(q, u0Var, objArr).invoke();
            return new G0(objArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static z0 i(u0 u0Var, j$.util.Q q, boolean z) {
        long I0 = u0Var.I0(q);
        if (I0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            z0 z0Var = (z0) new I0(0, q, u0Var).invoke();
            return z ? p(z0Var) : z0Var;
        } else if (I0 < 2147483639) {
            double[] dArr = new double[(int) I0];
            new l1(q, u0Var, dArr).invoke();
            return new Q0(dArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static A0 j(u0 u0Var, j$.util.Q q, boolean z) {
        long I0 = u0Var.I0(q);
        if (I0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            A0 a0 = (A0) new I0(1, q, u0Var).invoke();
            return z ? q(a0) : a0;
        } else if (I0 < 2147483639) {
            int[] iArr = new int[(int) I0];
            new m1(q, u0Var, iArr).invoke();
            return new Z0(iArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static B0 k(u0 u0Var, j$.util.Q q, boolean z) {
        long I0 = u0Var.I0(q);
        if (I0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            B0 b0 = (B0) new I0(2, q, u0Var).invoke();
            return z ? r(b0) : b0;
        } else if (I0 < 2147483639) {
            long[] jArr = new long[(int) I0];
            new n1(q, u0Var, jArr).invoke();
            return new i1(jArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static F0 l(U2 u2, D0 d0, D0 d02) {
        int i = E0.a[u2.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new L0((z0) d0, (z0) d02);
                    }
                    throw new IllegalStateException("Unknown shape " + u2);
                }
                return new N0((B0) d0, (B0) d02);
            }
            return new M0((A0) d0, (A0) d02);
        }
        return new P0(d0, d02);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static v0 m(long j) {
        return (j < 0 || j >= 2147483639) ? new S0() : new R0(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static X0 n(U2 u2) {
        Object obj;
        int i = E0.a[u2.ordinal()];
        if (i != 1) {
            if (i == 2) {
                obj = b;
            } else if (i == 3) {
                obj = c;
            } else if (i != 4) {
                throw new IllegalStateException("Unknown shape " + u2);
            } else {
                obj = d;
            }
            return (X0) obj;
        }
        return a;
    }

    public static D0 o(D0 d0, j$.util.function.N n) {
        if (d0.j() > 0) {
            long count = d0.count();
            if (count < 2147483639) {
                Object[] objArr = (Object[]) n.apply((int) count);
                new s1(d0, objArr).invoke();
                return new G0(objArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return d0;
    }

    public static z0 p(z0 z0Var) {
        if (z0Var.j() > 0) {
            long count = z0Var.count();
            if (count < 2147483639) {
                double[] dArr = new double[(int) count];
                new r1(z0Var, dArr).invoke();
                return new Q0(dArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return z0Var;
    }

    public static A0 q(A0 a0) {
        if (a0.j() > 0) {
            long count = a0.count();
            if (count < 2147483639) {
                int[] iArr = new int[(int) count];
                new r1(a0, iArr).invoke();
                return new Z0(iArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return a0;
    }

    public static B0 r(B0 b0) {
        if (b0.j() > 0) {
            long count = b0.count();
            if (count < 2147483639) {
                long[] jArr = new long[(int) count];
                new r1(b0, jArr).invoke();
                return new i1(jArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return b0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w0 s(long j) {
        return (j < 0 || j >= 2147483639) ? new b1() : new a1(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x0 t(long j) {
        return (j < 0 || j >= 2147483639) ? new k1() : new j1(j);
    }

    @Override // j$.util.stream.C3
    public Object a(u0 u0Var, j$.util.Q q) {
        return ((O1) new Q1(this, u0Var, q).invoke()).get();
    }

    @Override // j$.util.stream.C3
    public /* synthetic */ int b() {
        return 0;
    }

    @Override // j$.util.stream.C3
    public Object c(u0 u0Var, j$.util.Q q) {
        O1 u = u();
        u0Var.X0(q, u);
        return u.get();
    }

    public abstract O1 u();
}
