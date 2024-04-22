package j$.util;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class A extends E implements s.a {
    @Override // j$.util.s.a, j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.j(this, consumer);
    }

    @Override // j$.util.s.a
    public void e(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
    }

    @Override // j$.util.s.a, j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.b(this, consumer);
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

    @Override // j$.util.s.a
    public boolean k(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return false;
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ s.a trySplit() {
        return null;
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ t trySplit() {
        return null;
    }
}
