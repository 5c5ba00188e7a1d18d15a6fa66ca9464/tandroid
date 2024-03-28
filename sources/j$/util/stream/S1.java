package j$.util.stream;
/* loaded from: classes2.dex */
abstract class S1 extends D1 implements A1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public S1(A1 a1, A1 a12) {
        super(a1, a12);
    }

    @Override // j$.util.stream.A1
    public void d(Object obj, int i) {
        ((A1) this.a).d(obj, i);
        ((A1) this.b).d(obj, i + ((int) ((A1) this.a).count()));
    }

    @Override // j$.util.stream.A1
    public Object e() {
        long count = count();
        if (count < 2147483639) {
            Object c = c((int) count);
            d(c, 0);
            return c;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.A1
    public void g(Object obj) {
        ((A1) this.a).g(obj);
        ((A1) this.b).g(obj);
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ Object[] q(j$.util.function.m mVar) {
        return p1.g(this, mVar);
    }

    public String toString() {
        return count() < 32 ? String.format("%s[%s.%s]", getClass().getName(), this.a, this.b) : String.format("%s[size=%d]", getClass().getName(), Long.valueOf(count()));
    }
}
