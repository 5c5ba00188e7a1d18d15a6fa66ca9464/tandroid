package j$.util;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class C extends F implements t.b {
    @Override // j$.util.t.b, j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.k(this, consumer);
    }

    @Override // j$.util.t.b
    public void c(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
    }

    @Override // j$.util.t.b, j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.c(this, consumer);
    }

    @Override // j$.util.t.b
    public boolean g(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return false;
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

    @Override // j$.util.F, j$.util.t.a, j$.util.u, j$.util.t
    public /* bridge */ /* synthetic */ t.b trySplit() {
        return null;
    }

    @Override // j$.util.F, j$.util.t.a, j$.util.u, j$.util.t
    public /* bridge */ /* synthetic */ u trySplit() {
        return null;
    }
}
