package j$.util.stream;
/* loaded from: classes2.dex */
class J extends f3 {
    public final /* synthetic */ int b = 0;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(K k, m3 m3Var) {
        super(m3Var);
        this.c = k;
    }

    @Override // j$.util.stream.j3, j$.util.stream.m3
    public void accept(double d) {
        switch (this.b) {
            case 0:
                this.a.accept(((j$.wrappers.J) ((K) this.c).m).a(d));
                return;
            case 1:
                this.a.accept((m3) ((j$.util.function.g) ((L) this.c).m).apply(d));
                return;
            case 2:
                this.a.accept(((j$.wrappers.F) ((M) this.c).m).a(d));
                return;
            case 3:
                this.a.accept(((j$.util.function.h) ((N) this.c).m).applyAsLong(d));
                return;
            case 4:
                U u = (U) ((j$.util.function.g) ((K) this.c).m).apply(d);
                if (u != null) {
                    try {
                        u.sequential().j(new F(this));
                    } catch (Throwable th) {
                        try {
                            u.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (u != null) {
                    u.close();
                    return;
                }
                return;
            case 5:
                if (((j$.wrappers.D) ((K) this.c).m).b(d)) {
                    this.a.accept(d);
                    return;
                }
                return;
            default:
                ((j$.util.function.f) ((K) this.c).m).accept(d);
                this.a.accept(d);
                return;
        }
    }

    @Override // j$.util.stream.m3
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
    public J(K k, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = k;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(K k, m3 m3Var, j$.lang.b bVar) {
        super(m3Var);
        this.c = k;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(K k, m3 m3Var, j$.lang.c cVar) {
        super(m3Var);
        this.c = k;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }
}
