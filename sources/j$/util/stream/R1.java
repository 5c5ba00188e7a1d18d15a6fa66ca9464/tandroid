package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
final class R1 extends a2 {
    public final /* synthetic */ int b = 2;
    Object c;
    final /* synthetic */ b d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public R1(t tVar, e2 e2Var) {
        super(e2Var);
        this.d = tVar;
        this.c = new q(0, e2Var);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public R1(v vVar, e2 e2Var) {
        super(e2Var);
        this.d = vVar;
        this.c = new U(0, e2Var);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public R1(w wVar, e2 e2Var) {
        super(e2Var);
        this.d = wVar;
        this.c = new c0(0, e2Var);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.b) {
            case 0:
                LongStream longStream = (LongStream) ((Function) ((w) this.d).n).apply(obj);
                if (longStream != null) {
                    try {
                        longStream.sequential().d((c0) this.c);
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
            case 1:
                IntStream intStream = (IntStream) ((Function) ((v) this.d).n).apply(obj);
                if (intStream != null) {
                    try {
                        intStream.sequential().L((U) this.c);
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
                D d = (D) ((Function) ((t) this.d).n).apply(obj);
                if (d != null) {
                    try {
                        d.sequential().j((q) this.c);
                    } catch (Throwable th5) {
                        try {
                            d.close();
                        } catch (Throwable th6) {
                            th5.addSuppressed(th6);
                        }
                        throw th5;
                    }
                }
                if (d != null) {
                    d.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void n(long j) {
        switch (this.b) {
            case 0:
                this.a.n(-1L);
                return;
            case 1:
                this.a.n(-1L);
                return;
            default:
                this.a.n(-1L);
                return;
        }
    }
}
