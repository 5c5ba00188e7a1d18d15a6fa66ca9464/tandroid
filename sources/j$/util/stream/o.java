package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.Predicate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements Consumer, j$.util.function.y {
    public final /* synthetic */ int a = 5;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ o(BiConsumer biConsumer, Object obj) {
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
                return Objects.requireNonNull(consumer);
            case 5:
                return Objects.requireNonNull(consumer);
            default:
                return Objects.requireNonNull(consumer);
        }
    }

    @Override // j$.util.function.y
    public Object get() {
        switch (this.a) {
            case 1:
                return new i1((k1) this.b, (j$.wrappers.E) this.c);
            case 2:
                return new g1((k1) this.b, (j$.wrappers.V) this.c);
            case 3:
                return new h1((k1) this.b, (j$.wrappers.j0) this.c);
            default:
                return new f1((k1) this.b, (Predicate) this.c);
        }
    }

    public /* synthetic */ o(k1 k1Var, Predicate predicate) {
        this.b = k1Var;
        this.c = predicate;
    }

    public /* synthetic */ o(k1 k1Var, j$.wrappers.E e) {
        this.b = k1Var;
        this.c = e;
    }

    public /* synthetic */ o(k1 k1Var, j$.wrappers.V v) {
        this.b = k1Var;
        this.c = v;
    }

    public /* synthetic */ o(k1 k1Var, j$.wrappers.j0 j0Var) {
        this.b = k1Var;
        this.c = j0Var;
    }

    public /* synthetic */ o(m4 m4Var, Consumer consumer) {
        this.b = m4Var;
        this.c = consumer;
    }

    public /* synthetic */ o(AtomicBoolean atomicBoolean, ConcurrentHashMap concurrentHashMap) {
        this.b = atomicBoolean;
        this.c = concurrentHashMap;
    }
}
