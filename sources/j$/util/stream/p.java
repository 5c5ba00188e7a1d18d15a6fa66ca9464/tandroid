package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes2.dex */
public final /* synthetic */ class p implements Consumer, Supplier {
    public final /* synthetic */ int a = 5;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ p(BiConsumer biConsumer, Object obj) {
        this.b = biConsumer;
        this.c = obj;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.a) {
            case 0:
                AtomicBoolean atomicBoolean = (AtomicBoolean) this.b;
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.c;
                if (obj == null) {
                    atomicBoolean.set(true);
                    return;
                } else {
                    concurrentHashMap.putIfAbsent(obj, Boolean.TRUE);
                    return;
                }
            case 5:
                ((BiConsumer) this.b).accept(this.c, obj);
                return;
            default:
                ((m4) this.b).f((Consumer) this.c, obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 0:
                return Consumer.-CC.$default$andThen(this, consumer);
            case 5:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        switch (this.a) {
            case 1:
                return new i1((k1) this.b, (j$.wrappers.D) this.c);
            case 2:
                return new g1((k1) this.b, (j$.wrappers.U) this.c);
            case 3:
                return new h1((k1) this.b, (j$.wrappers.i0) this.c);
            default:
                return new f1((k1) this.b, (Predicate) this.c);
        }
    }

    public /* synthetic */ p(k1 k1Var, Predicate predicate) {
        this.b = k1Var;
        this.c = predicate;
    }

    public /* synthetic */ p(k1 k1Var, j$.wrappers.D d) {
        this.b = k1Var;
        this.c = d;
    }

    public /* synthetic */ p(k1 k1Var, j$.wrappers.U u) {
        this.b = k1Var;
        this.c = u;
    }

    public /* synthetic */ p(k1 k1Var, j$.wrappers.i0 i0Var) {
        this.b = k1Var;
        this.c = i0Var;
    }

    public /* synthetic */ p(m4 m4Var, Consumer consumer) {
        this.b = m4Var;
        this.c = consumer;
    }

    public /* synthetic */ p(AtomicBoolean atomicBoolean, ConcurrentHashMap concurrentHashMap) {
        this.b = atomicBoolean;
        this.c = concurrentHashMap;
    }
}
