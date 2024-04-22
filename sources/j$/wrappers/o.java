package j$.wrappers;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements j$.util.t {
    final /* synthetic */ Spliterator.OfPrimitive a;

    private /* synthetic */ o(Spliterator.OfPrimitive ofPrimitive) {
        this.a = ofPrimitive;
    }

    public static /* synthetic */ j$.util.t a(Spliterator.OfPrimitive ofPrimitive) {
        if (ofPrimitive == null) {
            return null;
        }
        return ofPrimitive instanceof p ? ((p) ofPrimitive).a : new o(ofPrimitive);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return this.a.tryAdvance($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.s
    public /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.s
    public /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfPrimitive) obj);
    }

    @Override // j$.util.s
    public /* synthetic */ Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return this.a.getExactSizeIfKnown();
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return this.a.hasCharacteristics(i);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfPrimitive) obj);
    }

    @Override // j$.util.s
    public /* synthetic */ j$.util.s trySplit() {
        return g.a(this.a.trySplit());
    }

    @Override // j$.util.t, j$.util.s
    public /* synthetic */ j$.util.t trySplit() {
        return a(this.a.trySplit());
    }
}
