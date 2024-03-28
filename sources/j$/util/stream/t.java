package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes2.dex */
class t extends d3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public t(c cVar, f4 f4Var, int i) {
        super(cVar, f4Var, i);
    }

    @Override // j$.util.stream.c
    B1 C0(z2 z2Var, j$.util.t tVar, j$.util.function.m mVar) {
        if (e4.DISTINCT.d(z2Var.q0())) {
            return z2Var.n0(tVar, false, mVar);
        }
        if (e4.ORDERED.d(z2Var.q0())) {
            return J0(z2Var, tVar);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new o0(new p(atomicBoolean, concurrentHashMap), false).c(z2Var, tVar);
        Collection keySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(keySet);
            hashSet.add(null);
            keySet = hashSet;
        }
        return new F1(keySet);
    }

    @Override // j$.util.stream.c
    j$.util.t D0(z2 z2Var, j$.util.t tVar) {
        return e4.DISTINCT.d(z2Var.q0()) ? z2Var.u0(tVar) : e4.ORDERED.d(z2Var.q0()) ? ((F1) J0(z2Var, tVar)).spliterator() : new n4(z2Var.u0(tVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        Objects.requireNonNull(n3Var);
        return e4.DISTINCT.d(i) ? n3Var : e4.SORTED.d(i) ? new r(this, n3Var) : new s(this, n3Var);
    }

    B1 J0(z2 z2Var, j$.util.t tVar) {
        q qVar = new Supplier() { // from class: j$.util.stream.q
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new LinkedHashSet();
            }
        };
        n nVar = new BiConsumer() { // from class: j$.util.stream.n
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LinkedHashSet) obj).add(obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        };
        return new F1((Collection) new A2(f4.REFERENCE, new BiConsumer() { // from class: j$.util.stream.o
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LinkedHashSet) obj).addAll((LinkedHashSet) obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        }, nVar, qVar).c(z2Var, tVar));
    }
}
