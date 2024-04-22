package j$.util.stream;

import j$.util.function.LongFunction;
/* loaded from: classes2.dex */
class a1 extends h3 {
    public final /* synthetic */ int b = 4;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    @Override // j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        switch (this.b) {
            case 0:
                this.a.accept(j);
                return;
            case 1:
                this.a.accept(((j$.util.function.s) ((O) this.c).m).applyAsLong(j));
                return;
            case 2:
                this.a.accept((m3) ((LongFunction) ((M) this.c).m).apply(j));
                return;
            case 3:
                this.a.accept(((j$.wrappers.m0) ((N) this.c).m).a(j));
                return;
            case 4:
                this.a.accept(((j$.wrappers.k0) ((L) this.c).m).a(j));
                return;
            case 5:
                LongStream longStream = (LongStream) ((LongFunction) ((O) this.c).m).apply(j);
                if (longStream != null) {
                    try {
                        longStream.sequential().d(new X0(this));
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
            case 6:
                if (((j$.wrappers.i0) ((O) this.c).m).b(j)) {
                    this.a.accept(j);
                    return;
                }
                return;
            default:
                ((j$.util.function.q) ((O) this.c).m).accept(j);
                this.a.accept(j);
                return;
        }
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        switch (this.b) {
            case 5:
                this.a.n(-1L);
                return;
            case 6:
                this.a.n(-1L);
                return;
            default:
                this.a.n(j);
                return;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, m3 m3Var) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, m3 m3Var, j$.lang.b bVar) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, m3 m3Var, j$.lang.c cVar) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(P p, m3 m3Var) {
        super(m3Var);
        this.c = p;
    }
}
