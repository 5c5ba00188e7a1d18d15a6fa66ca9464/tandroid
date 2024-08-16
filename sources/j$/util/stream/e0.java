package j$.util.stream;

import j$.util.function.LongFunction;
/* loaded from: classes2.dex */
final class e0 extends Z1 {
    public final /* synthetic */ int b;
    final /* synthetic */ b c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ e0(b bVar, e2 e2Var, int i) {
        super(e2Var);
        this.b = i;
        this.c = bVar;
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
        switch (this.b) {
            case 0:
                this.a.accept(((j$.util.function.f0) ((w) this.c).n).applyAsLong(j));
                return;
            case 1:
                this.a.accept((e2) ((LongFunction) ((u) this.c).n).apply(j));
                return;
            case 2:
                this.a.accept(((j$.util.function.b0) ((v) this.c).n).a.applyAsInt(j));
                return;
            case 3:
                this.a.accept(((j$.util.function.a0) ((t) this.c).n).a.applyAsDouble(j));
                return;
            case 4:
                LongStream longStream = (LongStream) ((LongFunction) ((w) this.c).n).apply(j);
                if (longStream != null) {
                    try {
                        longStream.sequential().d(new c0(1, this));
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
            case 5:
                if (((j$.util.function.Z) ((w) this.c).n).a.test(j)) {
                    this.a.accept(j);
                    return;
                }
                return;
            default:
                ((j$.util.function.W) ((w) this.c).n).accept(j);
                this.a.accept(j);
                return;
        }
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public void n(long j) {
        switch (this.b) {
            case 4:
                this.a.n(-1L);
                return;
            case 5:
                this.a.n(-1L);
                return;
            default:
                super.n(j);
                return;
        }
    }
}
