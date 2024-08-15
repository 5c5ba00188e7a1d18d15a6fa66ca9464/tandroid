package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class a0 extends a implements K {
    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.q(this, consumer);
    }

    @Override // j$.util.K
    public final void b(j$.util.function.h0 h0Var) {
        h0Var.getClass();
    }

    @Override // j$.util.K
    public final boolean e(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return false;
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.h(this, consumer);
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

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ K trySplit() {
        return null;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ N trySplit() {
        return null;
    }
}
