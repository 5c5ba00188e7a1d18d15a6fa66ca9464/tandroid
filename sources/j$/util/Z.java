package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class Z extends a implements H {
    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        a.f(this, consumer);
    }

    @Override // j$.util.H
    /* renamed from: c */
    public final void e(j$.util.function.F f) {
        f.getClass();
    }

    @Override // j$.util.H
    /* renamed from: g */
    public final boolean p(j$.util.function.F f) {
        f.getClass();
        return false;
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

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return a.o(this, consumer);
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
