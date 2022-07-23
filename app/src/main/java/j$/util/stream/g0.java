package j$.util.stream;
/* loaded from: classes2.dex */
final class g0 extends i0 implements l3 {
    @Override // j$.util.stream.i0, j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        accept(Long.valueOf(j));
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        qVar.getClass();
        return new j$.util.function.p(this, qVar);
    }

    @Override // j$.util.function.y
    public Object get() {
        if (this.a) {
            return j$.util.l.d(((Long) this.b).longValue());
        }
        return null;
    }
}
