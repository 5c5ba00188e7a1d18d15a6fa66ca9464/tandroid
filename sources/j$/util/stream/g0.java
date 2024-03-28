package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
final class g0 extends j0 implements l3 {
    @Override // j$.util.stream.j0, j$.util.stream.n3
    public void accept(int i) {
        accept(Integer.valueOf(i));
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        if (this.a) {
            return j$.util.k.d(((Integer) this.b).intValue());
        }
        return null;
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
