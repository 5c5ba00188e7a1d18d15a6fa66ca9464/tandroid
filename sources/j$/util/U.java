package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;

/* loaded from: classes2.dex */
final class U implements z, j$.util.function.W, j {
    boolean a = false;
    long b;
    final /* synthetic */ K c;

    U(K k) {
        this.c = k;
    }

    @Override // j$.util.z, j$.util.j
    public final void a(Consumer consumer) {
        if (consumer instanceof j$.util.function.W) {
            forEachRemaining((j$.util.function.W) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(U.class, "{0} calling PrimitiveIterator.OfLong.forEachRemainingLong(action::accept)");
            throw null;
        }
        forEachRemaining(new w(consumer));
    }

    @Override // j$.util.function.W
    public final void accept(long j) {
        this.a = true;
        this.b = j;
    }

    @Override // j$.util.A
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public final void forEachRemaining(j$.util.function.W w) {
        w.getClass();
        while (hasNext()) {
            w.accept(nextLong());
        }
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.p(this);
        }
        return this.a;
    }

    @Override // java.util.Iterator
    public final Long next() {
        if (!h0.a) {
            return Long.valueOf(nextLong());
        }
        h0.a(U.class, "{0} calling PrimitiveIterator.OfLong.nextLong()");
        throw null;
    }

    @Override // j$.util.z
    public final long nextLong() {
        if (!this.a && !hasNext()) {
            throw new NoSuchElementException();
        }
        this.a = false;
        return this.b;
    }
}
