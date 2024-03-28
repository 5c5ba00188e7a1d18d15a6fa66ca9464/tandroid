package j$.util.stream;

import j$.util.function.Function;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
class s extends j3 {
    public final /* synthetic */ int b = 3;
    Object c;
    final /* synthetic */ Object d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(t tVar, n3 n3Var) {
        super(n3Var);
        this.d = tVar;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.b) {
            case 0:
                if (((Set) this.c).contains(obj)) {
                    return;
                }
                ((Set) this.c).add(obj);
                this.a.accept((n3) obj);
                return;
            case 1:
                f1 f1Var = (f1) ((Function) ((O) this.d).m).apply(obj);
                if (f1Var != null) {
                    try {
                        f1Var.sequential().d((j$.util.function.q) this.c);
                    } catch (Throwable th) {
                        try {
                            f1Var.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (f1Var != null) {
                    f1Var.close();
                    return;
                }
                return;
            case 2:
                IntStream intStream = (IntStream) ((Function) ((N) this.d).m).apply(obj);
                if (intStream != null) {
                    try {
                        intStream.sequential().T((j$.util.function.l) this.c);
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
                V v = (V) ((Function) ((L) this.d).m).apply(obj);
                if (v != null) {
                    try {
                        v.sequential().j((j$.util.function.f) this.c);
                    } catch (Throwable th5) {
                        try {
                            v.close();
                        } catch (Throwable th6) {
                            th5.addSuppressed(th6);
                        }
                        throw th5;
                    }
                }
                if (v != null) {
                    v.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.j3, j$.util.stream.n3
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

    @Override // j$.util.stream.n3
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
    public s(L l, n3 n3Var) {
        super(n3Var);
        this.d = l;
        n3 n3Var2 = this.a;
        Objects.requireNonNull(n3Var2);
        this.c = new G(n3Var2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(N n, n3 n3Var) {
        super(n3Var);
        this.d = n;
        n3 n3Var2 = this.a;
        Objects.requireNonNull(n3Var2);
        this.c = new C0(n3Var2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(O o, n3 n3Var) {
        super(n3Var);
        this.d = o;
        n3 n3Var2 = this.a;
        Objects.requireNonNull(n3Var2);
        this.c = new X0(n3Var2);
    }
}
