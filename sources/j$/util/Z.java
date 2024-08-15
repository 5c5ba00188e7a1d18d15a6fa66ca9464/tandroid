package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class Z extends a implements H {
    @Override // j$.util.H, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.p(this, consumer);
    }

    @Override // j$.util.H
    public final void c(j$.util.function.K k) {
        k.getClass();
    }

    @Override // j$.util.H, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.g(this, consumer);
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.H
    public final boolean j(j$.util.function.K k) {
        k.getClass();
        return false;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ H trySplit() {
        return null;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ N trySplit() {
        return null;
    }
}
