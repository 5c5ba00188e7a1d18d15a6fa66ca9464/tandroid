package j$.util.stream;
/* loaded from: classes2.dex */
final class I extends L implements d2 {
    @Override // j$.util.stream.L, j$.util.stream.f2
    public final void accept(int i) {
        p(Integer.valueOf(i));
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return j$.util.m.d(((Integer) this.b).intValue());
        }
        return null;
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
