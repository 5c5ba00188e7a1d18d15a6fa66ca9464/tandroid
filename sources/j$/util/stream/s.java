package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes2.dex */
class s extends c3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public s(c cVar, e4 e4Var, int i) {
        super(cVar, e4Var, i);
    }

    @Override // j$.util.stream.c
    A1 E0(y2 y2Var, j$.util.t tVar, j$.util.function.m mVar) {
        if (d4.DISTINCT.d(y2Var.s0())) {
            return y2Var.p0(tVar, false, mVar);
        }
        if (d4.ORDERED.d(y2Var.s0())) {
            return L0(y2Var, tVar);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new n0(new o(atomicBoolean, concurrentHashMap), false).c(y2Var, tVar);
        Collection keySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(keySet);
            hashSet.add(null);
            keySet = hashSet;
        }
        return new E1(keySet);
    }

    @Override // j$.util.stream.c
    j$.util.t F0(y2 y2Var, j$.util.t tVar) {
        return d4.DISTINCT.d(y2Var.s0()) ? y2Var.w0(tVar) : d4.ORDERED.d(y2Var.s0()) ? ((E1) L0(y2Var, tVar)).spliterator() : new m4(y2Var.w0(tVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        Objects.requireNonNull(m3Var);
        return d4.DISTINCT.d(i) ? m3Var : d4.SORTED.d(i) ? new q(this, m3Var) : new r(this, m3Var);
    }

    A1 L0(y2 y2Var, j$.util.t tVar) {
        p pVar = p.a;
        m mVar = m.a;
        return new E1((Collection) new z2(e4.REFERENCE, n.a, mVar, pVar).c(y2Var, tVar));
    }
}
