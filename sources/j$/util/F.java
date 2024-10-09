package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;

/* loaded from: classes2.dex */
public final /* synthetic */ class F implements H {
    public final /* synthetic */ Spliterator.OfInt a;

    private /* synthetic */ F(Spliterator.OfInt ofInt) {
        this.a = ofInt;
    }

    public static /* synthetic */ H b(Spliterator.OfInt ofInt) {
        if (ofInt == null) {
            return null;
        }
        return ofInt instanceof G ? ((G) ofInt).a : new F(ofInt);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.H
    /* renamed from: c */
    public final /* synthetic */ void e(j$.util.function.F f) {
        this.a.forEachRemaining(j$.util.function.E.a(f));
    }

    @Override // j$.util.Q
    public final /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Spliterator.OfInt ofInt = this.a;
        if (obj instanceof F) {
            obj = ((F) obj).a;
        }
        return ofInt.equals(obj);
    }

    @Override // j$.util.Q
    public final /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.N
    /* renamed from: forEachRemaining */
    public final /* synthetic */ void e(Object obj) {
        this.a.forEachRemaining((Spliterator.OfInt) obj);
    }

    @Override // j$.util.H
    /* renamed from: g */
    public final /* synthetic */ boolean p(j$.util.function.F f) {
        return this.a.tryAdvance(j$.util.function.E.a(f));
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

    @Override // j$.util.N
    /* renamed from: tryAdvance */
    public final /* synthetic */ boolean p(Object obj) {
        return this.a.tryAdvance((Spliterator.OfInt) obj);
    }

    @Override // j$.util.H, j$.util.N, j$.util.Q
    public final /* synthetic */ H trySplit() {
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
