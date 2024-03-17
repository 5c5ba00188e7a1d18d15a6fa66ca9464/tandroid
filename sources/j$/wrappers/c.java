package j$.wrappers;

import j$.util.function.Consumer;
import j$.util.p;
import java.util.PrimitiveIterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class c implements p.a {
    final /* synthetic */ PrimitiveIterator.OfInt a;

    private /* synthetic */ c(PrimitiveIterator.OfInt ofInt) {
        this.a = ofInt;
    }

    public static /* synthetic */ p.a a(PrimitiveIterator.OfInt ofInt) {
        if (ofInt == null) {
            return null;
        }
        return ofInt instanceof d ? ((d) ofInt).a : new c(ofInt);
    }

    @Override // j$.util.p.a
    public /* synthetic */ void c(j$.util.function.l lVar) {
        this.a.forEachRemaining(Q.a(lVar));
    }

    @Override // j$.util.p.a, j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.p
    public /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((PrimitiveIterator.OfInt) obj);
    }

    @Override // java.util.Iterator
    public /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    @Override // j$.util.p.a, java.util.Iterator
    public /* synthetic */ Integer next() {
        return this.a.next();
    }

    @Override // java.util.Iterator
    public /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // j$.util.p.a
    public /* synthetic */ int nextInt() {
        return this.a.nextInt();
    }

    @Override // java.util.Iterator
    public /* synthetic */ void remove() {
        this.a.remove();
    }
}
