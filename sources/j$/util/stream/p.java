package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class p extends V1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public p(b bVar, int i) {
        super(bVar, i, 0);
    }

    static J0 G0(b bVar, j$.util.Q q) {
        l lVar = new l(17);
        l lVar2 = new l(18);
        return new J0((Collection) new u1(T2.REFERENCE, new l(19), lVar2, lVar, 3).c(bVar, q));
    }

    @Override // j$.util.stream.b
    final F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        if (S2.DISTINCT.d(bVar.s0())) {
            return bVar.k0(q, false, i);
        }
        if (S2.ORDERED.d(bVar.s0())) {
            return G0(bVar, q);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new P(new k0(5, atomicBoolean, concurrentHashMap), false).c(bVar, q);
        Collection keySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(keySet);
            hashSet.add(null);
            keySet = hashSet;
        }
        return new J0(keySet);
    }

    @Override // j$.util.stream.b
    final j$.util.Q x0(b bVar, j$.util.Q q) {
        return S2.DISTINCT.d(bVar.s0()) ? bVar.F0(q) : S2.ORDERED.d(bVar.s0()) ? G0(bVar, q).spliterator() : new b3(bVar.F0(q));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        e2Var.getClass();
        return S2.DISTINCT.d(i) ? e2Var : S2.SORTED.d(i) ? new n(e2Var) : new o(e2Var);
    }
}
