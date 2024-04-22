package j$.util;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class C extends E implements s.c {
    @Override // j$.util.s.c, j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.l(this, consumer);
    }

    @Override // j$.util.s.c
    public void d(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
    }

    @Override // j$.util.s.c, j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.d(this, consumer);
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

    @Override // j$.util.s.c
    public boolean i(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return false;
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ s.c trySplit() {
        return null;
    }

    @Override // j$.util.E, j$.util.s.a, j$.util.t, j$.util.s
    public /* bridge */ /* synthetic */ t trySplit() {
        return null;
    }
}
