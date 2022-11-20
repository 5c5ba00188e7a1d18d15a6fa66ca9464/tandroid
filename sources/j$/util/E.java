package j$.util;

import j$.util.function.Consumer;
import j$.util.u;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class E extends H implements u.a {
    @Override // j$.util.u.a, j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.k(this, consumer);
    }

    @Override // j$.util.u.a
    public void c(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
    }

    @Override // j$.util.u.a, j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.c(this, consumer);
    }

    @Override // j$.util.u.a
    public boolean g(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return false;
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

    @Override // j$.util.H, j$.util.t, j$.util.w, j$.util.u
    public /* bridge */ /* synthetic */ u.a trySplit() {
        return null;
    }

    @Override // j$.util.H, j$.util.t, j$.util.w, j$.util.u
    public /* bridge */ /* synthetic */ w trySplit() {
        return null;
    }
}
