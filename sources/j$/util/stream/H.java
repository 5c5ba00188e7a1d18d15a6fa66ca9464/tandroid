package j$.util.stream;
/* loaded from: classes2.dex */
final class H extends L implements c2 {
    @Override // j$.util.stream.L, j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        p(Double.valueOf(d));
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.l.d(((Double) this.b).doubleValue());
        }
        return null;
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }
}
