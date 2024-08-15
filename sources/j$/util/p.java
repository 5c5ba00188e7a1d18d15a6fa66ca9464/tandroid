package j$.util;

import j$.util.function.Consumer;
import java.util.PrimitiveIterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class p implements r, Iterator {
    public final /* synthetic */ PrimitiveIterator.OfDouble a;

    private /* synthetic */ p(PrimitiveIterator.OfDouble ofDouble) {
        this.a = ofDouble;
    }

    public static /* synthetic */ r a(PrimitiveIterator.OfDouble ofDouble) {
        if (ofDouble == null) {
            return null;
        }
        return ofDouble instanceof q ? ((q) ofDouble).a : new p(ofDouble);
    }

    @Override // j$.util.r
    public final /* synthetic */ void d(j$.util.function.m mVar) {
        this.a.forEachRemaining(j$.util.function.l.a(mVar));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof p) {
            obj = ((p) obj).a;
        }
        return this.a.equals(obj);
    }

    @Override // j$.util.r, j$.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.A
    public final /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((PrimitiveIterator.OfDouble) obj);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.r, java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ Double next() {
        return this.a.next();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // j$.util.r
    public final /* synthetic */ double nextDouble() {
        return this.a.nextDouble();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ void remove() {
        this.a.remove();
    }
}
