package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q extends V1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public q(c cVar, int i) {
        super(cVar, i);
    }

    static H0 q1(c cVar, j$.util.Q q) {
        J0 j0 = new J0(13);
        J0 j02 = new J0(14);
        return new H0((Collection) new v1(U2.REFERENCE, new J0(15), j02, j0, 3).a(cVar, q));
    }

    @Override // j$.util.stream.c
    final D0 i1(j$.util.Q q, j$.util.function.N n, c cVar) {
        if (T2.DISTINCT.d(cVar.K0())) {
            return cVar.Z0(q, false, n);
        }
        if (T2.ORDERED.d(cVar.K0())) {
            return q1(cVar, q);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new Q(new n(0, atomicBoolean, concurrentHashMap), false).a(cVar, q);
        Collection keySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(keySet);
            hashSet.add(null);
            keySet = hashSet;
        }
        return new H0(keySet);
    }

    @Override // j$.util.stream.c
    final j$.util.Q j1(c cVar, j$.util.Q q) {
        return T2.DISTINCT.d(cVar.K0()) ? cVar.p1(q) : T2.ORDERED.d(cVar.K0()) ? q1(cVar, q).spliterator() : new c3(cVar.p1(q));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        f2Var.getClass();
        return T2.DISTINCT.d(i) ? f2Var : T2.SORTED.d(i) ? new o(f2Var) : new p(this, f2Var);
    }
}
