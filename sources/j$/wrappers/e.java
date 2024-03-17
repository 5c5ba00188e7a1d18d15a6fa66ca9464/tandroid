package j$.wrappers;

import j$.util.function.Consumer;
import java.util.PrimitiveIterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class e implements j$.util.r {
    final /* synthetic */ PrimitiveIterator.OfLong a;

    private /* synthetic */ e(PrimitiveIterator.OfLong ofLong) {
        this.a = ofLong;
    }

    public static /* synthetic */ j$.util.r a(PrimitiveIterator.OfLong ofLong) {
        if (ofLong == null) {
            return null;
        }
        return ofLong instanceof f ? ((f) ofLong).a : new e(ofLong);
    }

    @Override // j$.util.r
    public /* synthetic */ void d(j$.util.function.q qVar) {
        this.a.forEachRemaining(f0.a(qVar));
    }

    @Override // j$.util.r, j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.p
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((PrimitiveIterator.OfLong) obj);
    }

    @Override // java.util.Iterator
    public /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    @Override // j$.util.r, java.util.Iterator
    public /* synthetic */ Long next() {
        return this.a.next();
    }

    @Override // java.util.Iterator
    public /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // j$.util.r
    public /* synthetic */ long nextLong() {
        return this.a.nextLong();
    }

    @Override // java.util.Iterator
    public /* synthetic */ void remove() {
        this.a.remove();
    }
}
