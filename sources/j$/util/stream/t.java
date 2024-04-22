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
class t extends c3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public t(c cVar, e4 e4Var, int i) {
        super(cVar, e4Var, i);
    }

    @Override // j$.util.stream.c
    j$.util.s A0(y2 y2Var, j$.util.s sVar) {
        return d4.DISTINCT.d(y2Var.n0()) ? y2Var.r0(sVar) : d4.ORDERED.d(y2Var.n0()) ? ((E1) G0(y2Var, sVar)).spliterator() : new m4(y2Var.r0(sVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 C0(int i, m3 m3Var) {
        Objects.requireNonNull(m3Var);
        return d4.DISTINCT.d(i) ? m3Var : d4.SORTED.d(i) ? new r(this, m3Var) : new s(this, m3Var);
    }

    A1 G0(y2 y2Var, j$.util.s sVar) {
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
        return new E1((Collection) new z2(e4.REFERENCE, new BiConsumer() { // from class: j$.util.stream.o
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LinkedHashSet) obj).addAll((LinkedHashSet) obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        }, nVar, qVar).c(y2Var, sVar));
    }

    @Override // j$.util.stream.c
    A1 z0(y2 y2Var, j$.util.s sVar, j$.util.function.m mVar) {
        if (d4.DISTINCT.d(y2Var.n0())) {
            return y2Var.k0(sVar, false, mVar);
        }
        if (d4.ORDERED.d(y2Var.n0())) {
            return G0(y2Var, sVar);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        new o0(new p(atomicBoolean, concurrentHashMap), false).c(y2Var, sVar);
        Collection keySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(keySet);
            hashSet.add(null);
            keySet = hashSet;
        }
        return new E1(keySet);
    }
}
