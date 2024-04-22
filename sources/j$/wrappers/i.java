package j$.wrappers;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class i implements s.a {
    final /* synthetic */ Spliterator.OfDouble a;

    private /* synthetic */ i(Spliterator.OfDouble ofDouble) {
        this.a = ofDouble;
    }

    public static /* synthetic */ s.a a(Spliterator.OfDouble ofDouble) {
        if (ofDouble == null) {
            return null;
        }
        return ofDouble instanceof j ? ((j) ofDouble).a : new i(ofDouble);
    }

    @Override // j$.util.s.a, j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return this.a.tryAdvance($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.s
    public /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.s.a
    public /* synthetic */ void e(j$.util.function.f fVar) {
        this.a.forEachRemaining(A.a(fVar));
    }

    @Override // j$.util.s
    public /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.s.a, j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfDouble) obj);
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

    @Override // j$.util.s.a
    public /* synthetic */ boolean k(j$.util.function.f fVar) {
        return this.a.tryAdvance(A.a(fVar));
    }

    @Override // j$.util.t
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfDouble) obj);
    }

    @Override // j$.util.s.a, j$.util.t, j$.util.s
    public /* synthetic */ s.a trySplit() {
        return a(this.a.trySplit());
    }

    @Override // j$.util.s
    public /* synthetic */ j$.util.s trySplit() {
        return g.a(this.a.trySplit());
    }

    @Override // j$.util.t, j$.util.s
    public /* synthetic */ j$.util.t trySplit() {
        return o.a(this.a.trySplit());
    }
}
