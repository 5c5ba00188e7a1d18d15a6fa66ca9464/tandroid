package j$.wrappers;

import j$.util.p;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class f implements PrimitiveIterator.OfLong {
    final /* synthetic */ p.b a;

    private /* synthetic */ f(p.b bVar) {
        this.a = bVar;
    }

    public static /* synthetic */ PrimitiveIterator.OfLong a(p.b bVar) {
        if (bVar == null) {
            return null;
        }
        return bVar instanceof e ? ((e) bVar).a : new f(bVar);
    }

    @Override // java.util.PrimitiveIterator
    public /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
        this.a.forEachRemaining(longConsumer);
    }

    @Override // java.util.PrimitiveIterator.OfLong, java.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(w.b(consumer));
    }

    @Override // java.util.PrimitiveIterator.OfLong
    public /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
        this.a.d(e0.b(longConsumer));
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$b] */
    @Override // java.util.Iterator
    public /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    @Override // java.util.PrimitiveIterator.OfLong, java.util.Iterator
    public /* synthetic */ Long next() {
        return this.a.next();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$b] */
    @Override // java.util.PrimitiveIterator.OfLong, java.util.Iterator
    public /* synthetic */ Object next() {
        return this.a.next();
    }

    @Override // java.util.PrimitiveIterator.OfLong
    public /* synthetic */ long nextLong() {
        return this.a.nextLong();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$b] */
    @Override // java.util.Iterator
    public /* synthetic */ void remove() {
        this.a.remove();
    }
}
