package j$.util.stream;

/* loaded from: classes2.dex */
final class s extends X1 {
    public final /* synthetic */ int b;
    final /* synthetic */ b c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ s(b bVar, e2 e2Var, int i) {
        super(e2Var);
        this.b = i;
        this.c = bVar;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        switch (this.b) {
            case 0:
                this.a.accept(((j$.util.function.w) ((t) this.c).n).a.applyAsDouble(d));
                return;
            case 1:
                this.a.r((e2) ((j$.util.function.q) ((u) this.c).n).apply(d));
                return;
            case 2:
                this.a.accept(((j$.util.function.s) ((v) this.c).n).a.applyAsInt(d));
                return;
            case 3:
                this.a.accept(((j$.util.function.v) ((w) this.c).n).applyAsLong(d));
                return;
            case 4:
                D d2 = (D) ((j$.util.function.q) ((t) this.c).n).apply(d);
                if (d2 != null) {
                    try {
                        d2.sequential().j(new q(1, this));
                    } catch (Throwable th) {
                        try {
                            d2.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (d2 != null) {
                    d2.close();
                    return;
                }
                return;
            case 5:
                if (((j$.util.function.r) ((t) this.c).n).a.test(d)) {
                    this.a.accept(d);
                    return;
                }
                return;
            default:
                ((j$.util.function.n) ((t) this.c).n).accept(d);
                this.a.accept(d);
                return;
        }
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public void n(long j) {
        switch (this.b) {
            case 4:
                this.a.n(-1L);
                break;
            case 5:
                this.a.n(-1L);
                break;
            default:
                super.n(j);
                break;
        }
    }
}
