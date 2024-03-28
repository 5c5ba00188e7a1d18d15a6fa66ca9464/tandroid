package j$.util.stream;

import j$.util.Optional;
/* loaded from: classes2.dex */
final class i0 extends j0 {
    @Override // j$.util.function.Supplier
    public Object get() {
        if (this.a) {
            return Optional.of(this.b);
        }
        return null;
    }
}
