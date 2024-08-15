package j$.util.concurrent;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class i extends b implements Iterator, Enumeration, j$.util.Iterator {
    public final /* synthetic */ int k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ i(m[] mVarArr, int i, int i2, ConcurrentHashMap concurrentHashMap, int i3) {
        super(mVarArr, i, i2, concurrentHashMap);
        this.k = i3;
    }

    @Override // j$.util.Iterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        switch (this.k) {
            case 0:
                Iterator.-CC.$default$forEachRemaining(this, consumer);
                return;
            default:
                Iterator.-CC.$default$forEachRemaining(this, consumer);
                return;
        }
    }

    @Override // java.util.Iterator
    public final /* synthetic */ void forEachRemaining(java.util.function.Consumer consumer) {
        switch (this.k) {
            case 0:
                forEachRemaining(Consumer.VivifiedWrapper.convert(consumer));
                return;
            default:
                forEachRemaining(Consumer.VivifiedWrapper.convert(consumer));
                return;
        }
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Object next() {
        switch (this.k) {
            case 0:
                m mVar = this.b;
                if (mVar != null) {
                    this.j = mVar;
                    f();
                    return mVar.b;
                }
                throw new NoSuchElementException();
            default:
                m mVar2 = this.b;
                if (mVar2 != null) {
                    Object obj = mVar2.c;
                    this.j = mVar2;
                    f();
                    return obj;
                }
                throw new NoSuchElementException();
        }
    }

    @Override // java.util.Enumeration
    public final Object nextElement() {
        switch (this.k) {
            case 0:
                return next();
            default:
                return next();
        }
    }
}
