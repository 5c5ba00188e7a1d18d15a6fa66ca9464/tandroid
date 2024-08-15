package j$.util.concurrent;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
final class e extends b implements Iterator, j$.util.Iterator {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e(m[] mVarArr, int i, int i2, ConcurrentHashMap concurrentHashMap) {
        super(mVarArr, i, i2, concurrentHashMap);
    }

    @Override // j$.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        Iterator.-CC.$default$forEachRemaining(this, consumer);
    }

    @Override // java.util.Iterator
    public final /* synthetic */ void forEachRemaining(java.util.function.Consumer consumer) {
        Iterator.-CC.$default$forEachRemaining(this, Consumer.VivifiedWrapper.convert(consumer));
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Object next() {
        m mVar = this.b;
        if (mVar != null) {
            Object obj = mVar.b;
            Object obj2 = mVar.c;
            this.j = mVar;
            f();
            return new l(obj, obj2, this.i);
        }
        throw new NoSuchElementException();
    }
}
