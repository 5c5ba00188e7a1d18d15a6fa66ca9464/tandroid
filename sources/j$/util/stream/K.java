package j$.util.stream;
/* loaded from: classes2.dex */
class K extends g3 {
    public final /* synthetic */ int b = 0;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(L l, n3 n3Var) {
        super(n3Var);
        this.c = l;
    }

    @Override // j$.util.stream.k3, j$.util.stream.n3
    public void accept(double d) {
        switch (this.b) {
            case 0:
                this.a.accept(((j$.wrappers.J) ((L) this.c).m).a(d));
                return;
            case 1:
                this.a.accept((n3) ((j$.util.function.g) ((M) this.c).m).apply(d));
                return;
            case 2:
                this.a.accept(((j$.wrappers.F) ((N) this.c).m).a(d));
                return;
            case 3:
                this.a.accept(((j$.util.function.h) ((O) this.c).m).applyAsLong(d));
                return;
            case 4:
                V v = (V) ((j$.util.function.g) ((L) this.c).m).apply(d);
                if (v != null) {
                    try {
                        v.sequential().j(new G(this));
                    } catch (Throwable th) {
                        try {
                            v.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (v != null) {
                    v.close();
                    return;
                }
                return;
            case 5:
                if (((j$.wrappers.D) ((L) this.c).m).b(d)) {
                    this.a.accept(d);
                    return;
                }
                return;
            default:
                ((j$.util.function.f) ((L) this.c).m).accept(d);
                this.a.accept(d);
                return;
        }
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        switch (this.b) {
            case 4:
                this.a.n(-1L);
                return;
            case 5:
                this.a.n(-1L);
                return;
            default:
                this.a.n(j);
                return;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(L l, n3 n3Var, j$.lang.a aVar) {
        super(n3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(L l, n3 n3Var, j$.lang.b bVar) {
        super(n3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(L l, n3 n3Var, j$.lang.c cVar) {
        super(n3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(M m, n3 n3Var) {
        super(n3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(N n, n3 n3Var) {
        super(n3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K(O o, n3 n3Var) {
        super(n3Var);
        this.c = o;
    }
}
