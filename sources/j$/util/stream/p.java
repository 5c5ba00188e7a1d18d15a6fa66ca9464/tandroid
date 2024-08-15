package j$.util.stream;

import j$.util.function.Function;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes2.dex */
final class p extends b2 {
    public final /* synthetic */ int b = 0;
    Object c;
    final /* synthetic */ c d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(q qVar, f2 f2Var) {
        super(f2Var);
        this.d = qVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(u uVar, f2 f2Var) {
        super(f2Var);
        this.d = uVar;
        this.c = new s(0, f2Var);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(w wVar, f2 f2Var) {
        super(f2Var);
        this.d = wVar;
        this.c = new V(0, f2Var);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(x xVar, f2 f2Var) {
        super(f2Var);
        this.d = xVar;
        this.c = new e0(0, f2Var);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        int i = this.b;
        c cVar = this.d;
        switch (i) {
            case 0:
                if (((Set) this.c).contains(obj)) {
                    return;
                }
                ((Set) this.c).add(obj);
                this.a.accept((f2) obj);
                return;
            case 1:
                LongStream longStream = (LongStream) ((Function) ((x) cVar).m).apply(obj);
                if (longStream != null) {
                    try {
                        longStream.sequential().E((j$.util.function.h0) this.c);
                    } catch (Throwable th) {
                        try {
                            longStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (longStream != null) {
                    longStream.close();
                    return;
                }
                return;
            case 2:
                IntStream intStream = (IntStream) ((Function) ((w) cVar).m).apply(obj);
                if (intStream != null) {
                    try {
                        intStream.sequential().V((j$.util.function.K) this.c);
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
                F f = (F) ((Function) ((u) cVar).m).apply(obj);
                if (f != null) {
                    try {
                        f.sequential().H((j$.util.function.m) this.c);
                    } catch (Throwable th5) {
                        try {
                            f.close();
                        } catch (Throwable th6) {
                            th5.addSuppressed(th6);
                        }
                        throw th5;
                    }
                }
                if (f != null) {
                    f.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.b2, j$.util.stream.f2
    public final void end() {
        switch (this.b) {
            case 0:
                this.c = null;
                this.a.end();
                return;
            default:
                super.end();
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        int i = this.b;
        f2 f2Var = this.a;
        switch (i) {
            case 0:
                this.c = new HashSet();
                f2Var.f(-1L);
                return;
            case 1:
                f2Var.f(-1L);
                return;
            case 2:
                f2Var.f(-1L);
                return;
            default:
                f2Var.f(-1L);
                return;
        }
    }
}
