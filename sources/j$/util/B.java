package j$.util;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class B extends F implements t.a {
    @Override // j$.util.t.a, j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.j(this, consumer);
    }

    @Override // j$.util.t.a
    public void e(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
    }

    @Override // j$.util.t.a, j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.b(this, consumer);
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.t.a
    public boolean k(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return false;
    }

    @Override // j$.util.F, j$.util.t.a, j$.util.u, j$.util.t
    public /* bridge */ /* synthetic */ t.a trySplit() {
        return null;
    }

    @Override // j$.util.F, j$.util.t.a, j$.util.u, j$.util.t
    public /* bridge */ /* synthetic */ u trySplit() {
        return null;
    }
}
