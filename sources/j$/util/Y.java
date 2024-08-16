package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class Y extends a implements E {
    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        a.b(this, consumer);
    }

    @Override // j$.util.E
    public final void e(j$.util.function.n nVar) {
        nVar.getClass();
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

    @Override // j$.util.E
    public final boolean p(j$.util.function.n nVar) {
        nVar.getClass();
        return false;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return a.n(this, consumer);
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
