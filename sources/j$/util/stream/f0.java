package j$.util.stream;
/* loaded from: classes2.dex */
final class f0 extends i0 implements k3 {
    @Override // j$.util.stream.i0, j$.util.stream.m3
    public void accept(int i) {
        accept(Integer.valueOf(i));
    }

    @Override // j$.util.function.y
    public Object get() {
        if (this.a) {
            return j$.util.k.d(((Integer) this.b).intValue());
        }
        return null;
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        lVar.getClass();
        return new j$.util.function.k(this, lVar);
    }
}
