package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class C implements E {
    public final /* synthetic */ Spliterator.OfDouble a;

    private /* synthetic */ C(Spliterator.OfDouble ofDouble) {
        this.a = ofDouble;
    }

    public static /* synthetic */ E b(Spliterator.OfDouble ofDouble) {
        if (ofDouble == null) {
            return null;
        }
        return ofDouble instanceof D ? ((D) ofDouble).a : new C(ofDouble);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.Q
    public final /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.E
    public final /* synthetic */ void e(j$.util.function.n nVar) {
        this.a.forEachRemaining(j$.util.function.m.a(nVar));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Spliterator.OfDouble ofDouble = this.a;
        if (obj instanceof C) {
            obj = ((C) obj).a;
        }
        return ofDouble.equals(obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.N
    public final /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfDouble) obj);
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

    @Override // j$.util.E
    public final /* synthetic */ boolean p(j$.util.function.n nVar) {
        return this.a.tryAdvance(j$.util.function.m.a(nVar));
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return this.a.tryAdvance(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.N
    public final /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfDouble) obj);
    }

    @Override // j$.util.E, j$.util.N, j$.util.Q
    public final /* synthetic */ E trySplit() {
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
