package j$.util.stream;
/* loaded from: classes2.dex */
class F0 extends g3 {
    public final /* synthetic */ int b = 0;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(K k, m3 m3Var) {
        super(m3Var);
        this.c = k;
    }

    @Override // j$.util.stream.k3, j$.util.stream.m3
    public void accept(int i) {
        switch (this.b) {
            case 0:
                this.a.accept(i);
                return;
            case 1:
                ((j$.util.function.l) ((M) this.c).m).accept(i);
                this.a.accept(i);
                return;
            case 2:
                this.a.accept(i);
                return;
            case 3:
                this.a.accept(((j$.wrappers.b0) ((M) this.c).m).a(i));
                return;
            case 4:
                this.a.accept((m3) ((j$.util.function.m) ((L) this.c).m).apply(i));
                return;
            case 5:
                this.a.accept(((j$.util.function.n) ((N) this.c).m).applyAsLong(i));
                return;
            case 6:
                this.a.accept(((j$.wrappers.X) ((K) this.c).m).a(i));
                return;
            case 7:
                IntStream intStream = (IntStream) ((j$.util.function.m) ((M) this.c).m).apply(i);
                if (intStream != null) {
                    try {
                        intStream.sequential().U(new B0(this));
                    } catch (Throwable th) {
                        try {
                            intStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (intStream == null) {
                    return;
                }
                intStream.close();
                return;
            default:
                if (!((j$.wrappers.V) ((M) this.c).m).b(i)) {
                    return;
                }
                this.a.accept(i);
                return;
        }
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        switch (this.b) {
            case 7:
                this.a.n(-1L);
                return;
            case 8:
                this.a.n(-1L);
                return;
            default:
                this.a.n(j);
                return;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(M m, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(M m, m3 m3Var, j$.lang.b bVar) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(M m, m3 m3Var, j$.lang.c cVar) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(O o, m3 m3Var) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public F0(G0 g0, m3 m3Var) {
        super(m3Var);
        this.c = g0;
    }
}
