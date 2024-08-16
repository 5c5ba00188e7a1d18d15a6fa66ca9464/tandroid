package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class a0 extends a implements K {
    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        a.h(this, consumer);
    }

    @Override // j$.util.K
    public final void d(j$.util.function.W w) {
        w.getClass();
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.K
    public final boolean i(j$.util.function.W w) {
        w.getClass();
        return false;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return a.q(this, consumer);
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ K trySplit() {
        return null;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ N trySplit() {
        return null;
    }
}
