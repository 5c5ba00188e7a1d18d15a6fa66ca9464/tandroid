package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class D extends H implements t {
    @Override // j$.util.t, j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.j(this, consumer);
    }

    @Override // j$.util.t
    public void e(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
    }

    @Override // j$.util.t, j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.b(this, consumer);
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.t
    public boolean k(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return false;
    }

    @Override // j$.util.H, j$.util.t, j$.util.w, j$.util.u
    public /* bridge */ /* synthetic */ t trySplit() {
        return null;
    }

    @Override // j$.util.H, j$.util.t, j$.util.w, j$.util.u
    public /* bridge */ /* synthetic */ w trySplit() {
        return null;
    }
}
