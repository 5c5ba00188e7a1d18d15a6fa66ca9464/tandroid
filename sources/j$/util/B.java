package j$.util;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class B extends E implements s.b {
    @Override // j$.util.s.b, j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.k(this, consumer);
    }

    @Override // j$.util.s.b
    public void c(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
    }

    @Override // j$.util.s.b, j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.c(this, consumer);
    }

    @Override // j$.util.s.b
    public boolean g(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return false;
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ s.b trySplit() {
        return null;
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ t trySplit() {
        return null;
    }
}
