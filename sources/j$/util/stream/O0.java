package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class O0 extends F0 implements C0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public O0(C0 c0, C0 c02) {
        super(c0, c02);
    }

    @Override // j$.util.stream.C0
    public final Object b() {
        long count = count();
        if (count < 2147483639) {
            Object newArray = newArray((int) count);
            c(newArray, 0);
            return newArray;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.C0
    public final void c(Object obj, int i) {
        D0 d0 = this.a;
        ((C0) d0).c(obj, i);
        ((C0) this.b).c(obj, i + ((int) ((C0) d0).count()));
    }

    @Override // j$.util.stream.C0
    public final void d(Object obj) {
        ((C0) this.a).d(obj);
        ((C0) this.b).d(obj);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ Object[] o(j$.util.function.N n) {
        return u0.r0(this, n);
    }

    public final String toString() {
        return count() < 32 ? String.format("%s[%s.%s]", getClass().getName(), this.a, this.b) : String.format("%s[size=%d]", getClass().getName(), Long.valueOf(count()));
    }
}
