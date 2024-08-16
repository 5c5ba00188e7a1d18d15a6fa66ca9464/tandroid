package j$.util;

import j$.util.function.Consumer;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
final class S implements Iterator, Consumer {
    boolean a = false;
    Object b;
    final /* synthetic */ Q c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public S(Q q) {
        this.c = q;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.a = true;
        this.b = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.s(this);
        }
        return this.a;
    }

    @Override // java.util.Iterator
    public final Object next() {
        if (this.a || hasNext()) {
            this.a = false;
            return this.b;
        }
        throw new NoSuchElementException();
    }
}
