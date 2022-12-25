package j$.util.stream;

import j$.util.function.Function;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
class r extends i3 {
    public final /* synthetic */ int b = 3;
    Object c;
    final /* synthetic */ Object d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(s sVar, m3 m3Var) {
        super(m3Var);
        this.d = sVar;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.b) {
            case 0:
                if (((Set) this.c).contains(obj)) {
                    return;
                }
                ((Set) this.c).add(obj);
                this.a.accept((m3) obj);
                return;
            case 1:
                e1 e1Var = (e1) ((Function) ((N) this.d).m).apply(obj);
                if (e1Var != null) {
                    try {
                        e1Var.sequential().d((j$.util.function.q) this.c);
                    } catch (Throwable th) {
                        try {
                            e1Var.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (e1Var != null) {
                    e1Var.close();
                    return;
                }
                return;
            case 2:
                IntStream intStream = (IntStream) ((Function) ((M) this.d).m).apply(obj);
                if (intStream != null) {
                    try {
                        intStream.sequential().U((j$.util.function.l) this.c);
                    } catch (Throwable th3) {
                        try {
                            intStream.close();
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                        throw th3;
                    }
                }
                if (intStream != null) {
                    intStream.close();
                    return;
                }
                return;
            default:
                U u = (U) ((Function) ((K) this.d).m).apply(obj);
                if (u != null) {
                    try {
                        u.sequential().j((j$.util.function.f) this.c);
                    } catch (Throwable th5) {
                        try {
                            u.close();
                        } catch (Throwable th6) {
                            th5.addSuppressed(th6);
                        }
                        throw th5;
                    }
                }
                if (u != null) {
                    u.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.i3, j$.util.stream.m3
    public void m() {
        switch (this.b) {
            case 0:
                this.c = null;
                this.a.m();
                return;
            default:
                this.a.m();
                return;
        }
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        switch (this.b) {
            case 0:
                this.c = new HashSet();
                this.a.n(-1L);
                return;
            case 1:
                this.a.n(-1L);
                return;
            case 2:
                this.a.n(-1L);
                return;
            default:
                this.a.n(-1L);
                return;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(K k, m3 m3Var) {
        super(m3Var);
        this.d = k;
        m3 m3Var2 = this.a;
        Objects.requireNonNull(m3Var2);
        this.c = new F(m3Var2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(M m, m3 m3Var) {
        super(m3Var);
        this.d = m;
        m3 m3Var2 = this.a;
        Objects.requireNonNull(m3Var2);
        this.c = new B0(m3Var2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(N n, m3 m3Var) {
        super(m3Var);
        this.d = n;
        m3 m3Var2 = this.a;
        Objects.requireNonNull(m3Var2);
        this.c = new W0(m3Var2);
    }
}
