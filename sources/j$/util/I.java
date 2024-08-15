package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class I implements K {
    public final /* synthetic */ Spliterator.OfLong a;

    private /* synthetic */ I(Spliterator.OfLong ofLong) {
        this.a = ofLong;
    }

    public static /* synthetic */ K f(Spliterator.OfLong ofLong) {
        if (ofLong == null) {
            return null;
        }
        return ofLong instanceof J ? ((J) ofLong).a : new I(ofLong);
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return this.a.tryAdvance(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.K
    public final /* synthetic */ void b(j$.util.function.h0 h0Var) {
        this.a.forEachRemaining(j$.util.function.g0.a(h0Var));
    }

    @Override // j$.util.Q
    public final /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.K
    public final /* synthetic */ boolean e(j$.util.function.h0 h0Var) {
        return this.a.tryAdvance(j$.util.function.g0.a(h0Var));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof I) {
            obj = ((I) obj).a;
        }
        return this.a.equals(obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.N
    public final /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfLong) obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return this.a.getExactSizeIfKnown();
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return this.a.hasCharacteristics(i);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.N
    public final /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfLong) obj);
    }

    @Override // j$.util.K, j$.util.N, j$.util.Q
    public final /* synthetic */ K trySplit() {
        return f(this.a.trySplit());
    }

    @Override // j$.util.N, j$.util.Q
    public final /* synthetic */ N trySplit() {
        return L.f(this.a.trySplit());
    }

    @Override // j$.util.Q
    public final /* synthetic */ Q trySplit() {
        return O.f(this.a.trySplit());
    }
}
