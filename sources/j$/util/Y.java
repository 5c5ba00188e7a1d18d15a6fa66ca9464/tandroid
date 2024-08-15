package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class Y extends a implements E {
    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.n(this, consumer);
    }

    @Override // j$.util.E
    public final void d(j$.util.function.m mVar) {
        mVar.getClass();
    }

    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.f(this, consumer);
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

    @Override // j$.util.E
    public final boolean o(j$.util.function.m mVar) {
        mVar.getClass();
        return false;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ E trySplit() {
        return null;
    }

    @Override // j$.util.a, j$.util.E, j$.util.N, j$.util.Q
    public final /* bridge */ /* synthetic */ N trySplit() {
        return null;
    }
}
