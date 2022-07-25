package j$.wrappers;

import j$.util.p;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class d implements PrimitiveIterator.OfInt {
    final /* synthetic */ p.a a;

    private /* synthetic */ d(p.a aVar) {
        this.a = aVar;
    }

    public static /* synthetic */ PrimitiveIterator.OfInt a(p.a aVar) {
        if (aVar == null) {
            return null;
        }
        return aVar instanceof c ? ((c) aVar).a : new d(aVar);
    }

    @Override // java.util.PrimitiveIterator
    public /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
        this.a.forEachRemaining(intConsumer);
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        this.a.forEachRemaining(w.b(consumer));
    }

    @Override // java.util.PrimitiveIterator.OfInt
    /* renamed from: forEachRemaining  reason: avoid collision after fix types in other method */
    public /* synthetic */ void forEachRemaining2(IntConsumer intConsumer) {
        this.a.c(Q.b(intConsumer));
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$a] */
    @Override // java.util.Iterator
    public /* synthetic */ boolean hasNext() {
        return this.a.hasNext();
    }

    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    public /* synthetic */ Integer next() {
        return this.a.mo337next();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$a] */
    @Override // java.util.PrimitiveIterator.OfInt, java.util.Iterator
    /* renamed from: next  reason: collision with other method in class */
    public /* synthetic */ Object mo338next() {
        return this.a.mo335next();
    }

    @Override // java.util.PrimitiveIterator.OfInt
    public /* synthetic */ int nextInt() {
        return this.a.nextInt();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.Iterator, j$.util.p$a] */
    @Override // java.util.Iterator
    public /* synthetic */ void remove() {
        this.a.remove();
    }
}
