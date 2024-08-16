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

    public static /* synthetic */ K b(Spliterator.OfLong ofLong) {
        if (ofLong == null) {
            return null;
        }
        return ofLong instanceof J ? ((J) ofLong).a : new I(ofLong);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.Q
    public final /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.K
    public final /* synthetic */ void d(j$.util.function.W w) {
        this.a.forEachRemaining(j$.util.function.V.a(w));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Spliterator.OfLong ofLong = this.a;
        if (obj instanceof I) {
            obj = ((I) obj).a;
        }
        return ofLong.equals(obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
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

    @Override // j$.util.K
    public final /* synthetic */ boolean i(j$.util.function.W w) {
        return this.a.tryAdvance(j$.util.function.V.a(w));
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return this.a.tryAdvance(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.N
    public final /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfLong) obj);
    }

    @Override // j$.util.K, j$.util.N, j$.util.Q
    public final /* synthetic */ K trySplit() {
        return b(this.a.trySplit());
    }

    @Override // j$.util.N, j$.util.Q
    public final /* synthetic */ N trySplit() {
        return L.b(this.a.trySplit());
    }

    @Override // j$.util.Q
    public final /* synthetic */ Q trySplit() {
        return O.b(this.a.trySplit());
    }
}
