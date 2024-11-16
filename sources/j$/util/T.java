package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;

/* loaded from: classes2.dex */
final class T implements v, j$.util.function.F, j {
    boolean a = false;
    int b;
    final /* synthetic */ H c;

    T(H h) {
        this.c = h;
    }

    @Override // j$.util.v, j$.util.j
    public final void a(Consumer consumer) {
        if (consumer instanceof j$.util.function.F) {
            forEachRemaining((j$.util.function.F) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(T.class, "{0} calling PrimitiveIterator.OfInt.forEachRemainingInt(action::accept)");
            throw null;
        }
        forEachRemaining(new s(consumer));
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        this.a = true;
        this.b = i;
    }

    @Override // j$.util.A
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public final void forEachRemaining(j$.util.function.F f) {
        f.getClass();
        while (hasNext()) {
            f.accept(nextInt());
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.p(this);
        }
        return this.a;
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // java.util.Iterator
    public final Integer next() {
        if (!h0.a) {
            return Integer.valueOf(nextInt());
        }
        h0.a(T.class, "{0} calling PrimitiveIterator.OfInt.nextInt()");
        throw null;
    }

    @Override // j$.util.v
    public final int nextInt() {
        if (!this.a && !hasNext()) {
            throw new NoSuchElementException();
        }
        this.a = false;
        return this.b;
    }
}
