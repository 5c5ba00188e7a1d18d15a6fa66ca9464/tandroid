package j$.wrappers;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class m implements t.c {
    final /* synthetic */ Spliterator.OfLong a;

    private /* synthetic */ m(Spliterator.OfLong ofLong) {
        this.a = ofLong;
    }

    public static /* synthetic */ t.c a(Spliterator.OfLong ofLong) {
        if (ofLong == null) {
            return null;
        }
        return ofLong instanceof n ? ((n) ofLong).a : new m(ofLong);
    }

    @Override // j$.util.t.c, j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return this.a.tryAdvance($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.t
    public /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.t.c
    public /* synthetic */ void d(j$.util.function.q qVar) {
        this.a.forEachRemaining(f0.a(qVar));
    }

    @Override // j$.util.t
    public /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.t.c, j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.u
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfLong) obj);
    }

    @Override // j$.util.t
    public /* synthetic */ Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return this.a.getExactSizeIfKnown();
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return this.a.hasCharacteristics(i);
    }

    @Override // j$.util.t.c
    public /* synthetic */ boolean i(j$.util.function.q qVar) {
        return this.a.tryAdvance(f0.a(qVar));
    }

    @Override // j$.util.u
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfLong) obj);
    }

    @Override // j$.util.t.c, j$.util.u, j$.util.t
    public /* synthetic */ t.c trySplit() {
        return a(this.a.trySplit());
    }

    @Override // j$.util.t.c, j$.util.u, j$.util.t
    public /* synthetic */ j$.util.t trySplit() {
        return g.a(this.a.trySplit());
    }

    @Override // j$.util.t.c, j$.util.u, j$.util.t
    public /* synthetic */ j$.util.u trySplit() {
        return o.a(this.a.trySplit());
    }
}
