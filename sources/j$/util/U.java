package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
final class U implements z, j$.util.function.h0, Iterator {
    boolean a = false;
    long b;
    final /* synthetic */ K c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public U(K k) {
        this.c = k;
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        this.a = true;
        this.b = j;
    }

    @Override // j$.util.A
    /* renamed from: b */
    public final void forEachRemaining(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        while (hasNext()) {
            h0Var.accept(nextLong());
        }
    }

    @Override // j$.util.z, j$.util.Iterator
    public final void forEachRemaining(Consumer consumer) {
        if (consumer instanceof j$.util.function.h0) {
            forEachRemaining((j$.util.function.h0) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(U.class, "{0} calling PrimitiveIterator.OfLong.forEachRemainingLong(action::accept)");
            throw null;
        } else {
            forEachRemaining(new w(consumer));
        }
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.e(this);
        }
        return this.a;
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Long next() {
        if (h0.a) {
            h0.a(U.class, "{0} calling PrimitiveIterator.OfLong.nextLong()");
            throw null;
        }
        return Long.valueOf(nextLong());
    }

    @Override // j$.util.z
    public final long nextLong() {
        if (this.a || hasNext()) {
            this.a = false;
            return this.b;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
