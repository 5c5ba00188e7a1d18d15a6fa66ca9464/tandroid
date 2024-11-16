package j$.util.concurrent;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes2.dex */
final class g extends a implements Iterator, Enumeration {
    public final /* synthetic */ int k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ g(k[] kVarArr, int i, int i2, ConcurrentHashMap concurrentHashMap, int i3) {
        super(kVarArr, i, i2, concurrentHashMap);
        this.k = i3;
    }

    @Override // java.util.Iterator
    public final Object next() {
        switch (this.k) {
            case 0:
                k kVar = this.b;
                if (kVar == null) {
                    throw new NoSuchElementException();
                }
                this.j = kVar;
                b();
                return kVar.b;
            default:
                k kVar2 = this.b;
                if (kVar2 == null) {
                    throw new NoSuchElementException();
                }
                Object obj = kVar2.c;
                this.j = kVar2;
                b();
                return obj;
        }
    }

    @Override // java.util.Enumeration
    public final Object nextElement() {
        switch (this.k) {
        }
        return next();
    }
}
