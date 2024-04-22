package j$.util.stream;
/* loaded from: classes2.dex */
class G0 extends g3 {
    public final /* synthetic */ int b = 0;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    @Override // j$.util.stream.k3, j$.util.stream.m3
    public void accept(int i) {
        switch (this.b) {
            case 0:
                this.a.accept(i);
                return;
            case 1:
                ((j$.util.function.l) ((N) this.c).m).accept(i);
                this.a.accept(i);
                return;
            case 2:
                this.a.accept(i);
                return;
            case 3:
                this.a.accept(((j$.wrappers.a0) ((N) this.c).m).a(i));
                return;
            case 4:
                this.a.accept((m3) ((j$.util.function.m) ((M) this.c).m).apply(i));
                return;
            case 5:
                this.a.accept(((j$.util.function.n) ((O) this.c).m).applyAsLong(i));
                return;
            case 6:
                this.a.accept(((j$.wrappers.W) ((L) this.c).m).a(i));
                return;
            case 7:
                IntStream intStream = (IntStream) ((j$.util.function.m) ((N) this.c).m).apply(i);
                if (intStream != null) {
                    try {
                        intStream.sequential().R(new C0(this));
                    } catch (Throwable th) {
                        try {
                            intStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (intStream != null) {
                    intStream.close();
                    return;
                }
                return;
            default:
                if (((j$.wrappers.U) ((N) this.c).m).b(i)) {
                    this.a.accept(i);
                    return;
                }
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
    public G0(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(N n, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(N n, m3 m3Var, j$.lang.b bVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(N n, m3 m3Var, j$.lang.c cVar) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(O o, m3 m3Var) {
        super(m3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(P p, m3 m3Var) {
        super(m3Var);
        this.c = p;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G0(H0 h0, m3 m3Var) {
        super(m3Var);
        this.c = h0;
    }
}
