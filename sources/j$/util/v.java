package j$.util;

import j$.util.Iterator;
import j$.util.function.Consumer;
import j$.util.p;
import j$.util.s;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: classes2.dex */
class v implements p.a, j$.util.function.l, Iterator {
    boolean a = false;
    int b;
    final /* synthetic */ s.b c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(s.b bVar) {
        this.c = bVar;
    }

    @Override // j$.util.function.l
    public void accept(int i) {
        this.a = true;
        this.b = i;
    }

    @Override // j$.util.p
    /* renamed from: c */
    public void forEachRemaining(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        while (hasNext()) {
            lVar.accept(nextInt());
        }
    }

    @Override // j$.util.p.a, j$.util.Iterator
    public void forEachRemaining(Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            forEachRemaining((j$.util.function.l) consumer);
            return;
        }
        Objects.requireNonNull(consumer);
        if (!K.a) {
            while (hasNext()) {
                consumer.accept(Integer.valueOf(nextInt()));
            }
            return;
        }
        K.a(v.class, "{0} calling PrimitiveIterator.OfInt.forEachRemainingInt(action::accept)");
        throw null;
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public boolean hasNext() {
        if (!this.a) {
            this.c.g(this);
        }
        return this.a;
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public Integer next() {
        if (K.a) {
            K.a(v.class, "{0} calling PrimitiveIterator.OfInt.nextInt()");
            throw null;
        }
        return Integer.valueOf(nextInt());
    }

    @Override // j$.util.p.a
    public int nextInt() {
        if (this.a || hasNext()) {
            this.a = false;
            return this.b;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public /* synthetic */ void remove() {
        Iterator.-CC.a(this);
        throw null;
    }
}
