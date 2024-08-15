package j$.util;

import j$.util.function.Consumer;
import java.util.PrimitiveIterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class t implements v, Iterator {
    public final /* synthetic */ PrimitiveIterator.OfInt a;

    private /* synthetic */ t(PrimitiveIterator.OfInt ofInt) {
        this.a = ofInt;
    }

    public static /* synthetic */ v a(PrimitiveIterator.OfInt ofInt) {
        if (ofInt == null) {
            return null;
        }
        return ofInt instanceof u ? ((u) ofInt).a : new t(ofInt);
    }

    @Override // j$.util.v
    public final /* synthetic */ void c(j$.util.function.K k) {
        this.a.forEachRemaining(j$.util.function.J.a(k));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof t) {
            obj = ((t) obj).a;
        }
        return this.a.equals(obj);
    }

    @Override // j$.util.v, j$.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(Consumer.Wrapper.convert(consumer));
    }

    @Override // j$.util.A
    public final /* synthetic */ void forEachRemaining(Object obj) {
        this.a.forEachRemaining((PrimitiveIterator.OfInt) obj);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.v, java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ Integer next() {
        return this.a.next();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // j$.util.v
    public final /* synthetic */ int nextInt() {
        return this.a.nextInt();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* synthetic */ void remove() {
        this.a.remove();
    }
}
