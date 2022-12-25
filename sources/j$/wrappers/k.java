package j$.wrappers;

import j$.util.function.Consumer;
import j$.util.u;
import java.util.Comparator;
import java.util.Spliterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class k implements u.a {
    final /* synthetic */ Spliterator.OfInt a;

    private /* synthetic */ k(Spliterator.OfInt ofInt) {
        this.a = ofInt;
    }

    public static /* synthetic */ u.a a(Spliterator.OfInt ofInt) {
        if (ofInt == null) {
            return null;
        }
        return ofInt instanceof l ? ((l) ofInt).a : new k(ofInt);
    }

    @Override // j$.util.u.a, j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return this.a.tryAdvance($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.u.a
    public /* synthetic */ void c(j$.util.function.l lVar) {
        this.a.forEachRemaining(Q.a(lVar));
    }

    @Override // j$.util.u
    public /* synthetic */ int characteristics() {
        return this.a.characteristics();
    }

    @Override // j$.util.u
    public /* synthetic */ long estimateSize() {
        return this.a.estimateSize();
    }

    @Override // j$.util.u.a, j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.w
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((Spliterator.OfInt) obj);
    }

    @Override // j$.util.u.a
    public /* synthetic */ boolean g(j$.util.function.l lVar) {
        return this.a.tryAdvance(Q.a(lVar));
    }

    @Override // j$.util.u
    public /* synthetic */ Comparator getComparator() {
        return this.a.getComparator();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return this.a.getExactSizeIfKnown();
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return this.a.hasCharacteristics(i);
    }

    @Override // j$.util.w
    public /* synthetic */ boolean tryAdvance(Object obj) {
        return this.a.tryAdvance((Spliterator.OfInt) obj);
    }

    @Override // j$.util.u.a, j$.util.w, j$.util.u
    public /* synthetic */ u.a trySplit() {
        return a(this.a.trySplit());
    }

    @Override // j$.util.u.a, j$.util.w, j$.util.u
    public /* synthetic */ j$.util.u trySplit() {
        return g.a(this.a.trySplit());
    }

    @Override // j$.util.u.a, j$.util.w, j$.util.u
    public /* synthetic */ j$.util.w trySplit() {
        return o.a(this.a.trySplit());
    }
}
