package j$.util;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class u implements PrimitiveIterator.OfInt {
    public final /* synthetic */ v a;

    private /* synthetic */ u(v vVar) {
        this.a = vVar;
    }

    public static /* synthetic */ PrimitiveIterator.OfInt b(v vVar) {
        if (vVar == null) {
            return null;
        }
        return vVar instanceof t ? ((t) vVar).a : new u(vVar);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        v vVar = this.a;
        if (obj instanceof u) {
            obj = ((u) obj).a;
        }
        return vVar.equals(obj);
    }

    @Override // java.util.PrimitiveIterator
    public final /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
        this.a.forEachRemaining(intConsumer);
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.a(j$.util.function.g.a(consumer));
    }

    @Override // java.util.PrimitiveIterator.OfInt
    public final /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
        this.a.c(j$.util.function.D.a(intConsumer));
    }

    @Override // java.util.Iterator
    public final /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    public final /* synthetic */ Integer next() {
        return this.a.next();
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    public final /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // java.util.PrimitiveIterator.OfInt
    public final /* synthetic */ int nextInt() {
        return this.a.nextInt();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ void remove() {
        this.a.remove();
    }
}
