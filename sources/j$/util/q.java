package j$.util;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class q implements PrimitiveIterator.OfDouble {
    public final /* synthetic */ r a;

    private /* synthetic */ q(r rVar) {
        this.a = rVar;
    }

    public static /* synthetic */ PrimitiveIterator.OfDouble b(r rVar) {
        if (rVar == null) {
            return null;
        }
        return rVar instanceof p ? ((p) rVar).a : new q(rVar);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        r rVar = this.a;
        if (obj instanceof q) {
            obj = ((q) obj).a;
        }
        return rVar.equals(obj);
    }

    @Override // java.util.PrimitiveIterator
    public final /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
        this.a.forEachRemaining(doubleConsumer);
    }

    @Override // java.util.PrimitiveIterator.OfDouble, java.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.a(j$.util.function.g.a(consumer));
    }

    @Override // java.util.PrimitiveIterator.OfDouble
    public final /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
        this.a.e(j$.util.function.l.a(doubleConsumer));
    }

    @Override // java.util.Iterator
    public final /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.PrimitiveIterator.OfDouble, java.util.Iterator
    public final /* synthetic */ Double next() {
        return this.a.next();
    }

    @Override // java.util.PrimitiveIterator.OfDouble, java.util.Iterator
    public final /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // java.util.PrimitiveIterator.OfDouble
    public final /* synthetic */ double nextDouble() {
        return this.a.nextDouble();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ void remove() {
        this.a.remove();
    }
}
