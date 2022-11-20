package j$.util.stream;
/* loaded from: classes2.dex */
class Z0 extends h3 {
    public final /* synthetic */ int b = 4;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(K k, m3 m3Var) {
        super(m3Var);
        this.c = k;
    }

    @Override // j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        switch (this.b) {
            case 0:
                this.a.accept(j);
                return;
            case 1:
                this.a.accept(((j$.util.function.t) ((N) this.c).m).applyAsLong(j));
                return;
            case 2:
                this.a.accept((m3) ((j$.util.function.r) ((L) this.c).m).apply(j));
                return;
            case 3:
                this.a.accept(((j$.wrappers.n0) ((M) this.c).m).a(j));
                return;
            case 4:
                this.a.accept(((j$.wrappers.l0) ((K) this.c).m).a(j));
                return;
            case 5:
                e1 e1Var = (e1) ((j$.util.function.r) ((N) this.c).m).apply(j);
                if (e1Var != null) {
                    try {
                        e1Var.sequential().d(new W0(this));
                    } catch (Throwable th) {
                        try {
                            e1Var.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (e1Var == null) {
                    return;
                }
                e1Var.close();
                return;
            case 6:
                if (!((j$.wrappers.j0) ((N) this.c).m).b(j)) {
                    return;
                }
                this.a.accept(j);
                return;
            default:
                ((j$.util.function.q) ((N) this.c).m).accept(j);
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
    public Z0(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(N n, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(N n, m3 m3Var, j$.lang.b bVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(N n, m3 m3Var, j$.lang.c cVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z0(O o, m3 m3Var) {
        super(m3Var);
        this.c = o;
    }
}
