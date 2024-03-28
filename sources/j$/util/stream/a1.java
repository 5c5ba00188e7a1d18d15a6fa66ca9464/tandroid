package j$.util.stream;
/* loaded from: classes2.dex */
class a1 extends i3 {
    public final /* synthetic */ int b = 4;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(L l, n3 n3Var) {
        super(n3Var);
        this.c = l;
    }

    @Override // j$.util.stream.m3, j$.util.function.q
    public void accept(long j) {
        switch (this.b) {
            case 0:
                this.a.accept(j);
                return;
            case 1:
                this.a.accept(((j$.util.function.t) ((O) this.c).m).applyAsLong(j));
                return;
            case 2:
                this.a.accept((n3) ((j$.util.function.r) ((M) this.c).m).apply(j));
                return;
            case 3:
                this.a.accept(((j$.wrappers.m0) ((N) this.c).m).a(j));
                return;
            case 4:
                this.a.accept(((j$.wrappers.k0) ((L) this.c).m).a(j));
                return;
            case 5:
                f1 f1Var = (f1) ((j$.util.function.r) ((O) this.c).m).apply(j);
                if (f1Var != null) {
                    try {
                        f1Var.sequential().d(new X0(this));
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

    @Override // j$.util.stream.n3
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
    public a1(M m, n3 n3Var) {
        super(n3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(N n, n3 n3Var) {
        super(n3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, n3 n3Var) {
        super(n3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, n3 n3Var, j$.lang.a aVar) {
        super(n3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, n3 n3Var, j$.lang.b bVar) {
        super(n3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(O o, n3 n3Var, j$.lang.c cVar) {
        super(n3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a1(P p, n3 n3Var) {
        super(n3Var);
        this.c = p;
    }
}
