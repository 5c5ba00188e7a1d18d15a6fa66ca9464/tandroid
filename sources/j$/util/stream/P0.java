package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class P0 extends H0 implements E0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public P0(E0 e0, E0 e02) {
        super(e0, e02);
    }

    @Override // j$.util.stream.E0
    public final void d(Object obj, int i) {
        F0 f0 = this.a;
        ((E0) f0).d(obj, i);
        ((E0) this.b).d(obj, i + ((int) ((E0) f0).count()));
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        long count = count();
        if (count >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object c = c((int) count);
        d(c, 0);
        return c;
    }

    @Override // j$.util.stream.E0
    public final void g(Object obj) {
        ((E0) this.a).g(obj);
        ((E0) this.b).g(obj);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ Object[] s(j$.util.function.I i) {
        return t0.m(this, i);
    }

    public final String toString() {
        long count = count();
        String name = getClass().getName();
        return count < 32 ? String.format("%s[%s.%s]", name, this.a, this.b) : String.format("%s[size=%d]", name, Long.valueOf(count()));
    }
}
