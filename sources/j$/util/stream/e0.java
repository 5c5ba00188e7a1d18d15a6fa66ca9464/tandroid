package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
final class e0 extends i0 implements j3 {
    @Override // j$.util.stream.i0, j$.util.stream.m3
    public void accept(double d) {
        accept(Double.valueOf(d));
    }

    @Override // j$.util.function.y
    public Object get() {
        if (this.a) {
            return j$.util.j.d(((Double) this.b).doubleValue());
        }
        return null;
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
