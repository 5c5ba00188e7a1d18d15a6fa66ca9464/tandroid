package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;

/* loaded from: classes2.dex */
public final /* synthetic */ class O implements Q {
    public final /* synthetic */ Spliterator a;

    private /* synthetic */ O(Spliterator spliterator) {
        this.a = spliterator;
    }

    public static /* synthetic */ Q b(Spliterator spliterator) {
        if (spliterator == null) {
            return null;
        }
        return spliterator instanceof P ? ((P) spliterator).a : spliterator instanceof Spliterator.OfPrimitive ? L.b((Spliterator.OfPrimitive) spliterator) : new O(spliterator);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.Q
    public final /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Spliterator spliterator = this.a;
        if (obj instanceof O) {
            obj = ((O) obj).a;
        }
        return spliterator.equals(obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
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

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return this.a.tryAdvance(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.Q
    public final /* synthetic */ Q trySplit() {
        return b(this.a.trySplit());
    }
}
