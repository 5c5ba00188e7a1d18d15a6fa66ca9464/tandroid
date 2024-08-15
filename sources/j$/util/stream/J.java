package j$.util.stream;
/* loaded from: classes2.dex */
final class J extends L implements e2 {
    @Override // j$.util.stream.L, j$.util.stream.f2
    public final void accept(long j) {
        p(Long.valueOf(j));
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.n.d(((Long) this.b).longValue());
        }
        return null;
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }
}
