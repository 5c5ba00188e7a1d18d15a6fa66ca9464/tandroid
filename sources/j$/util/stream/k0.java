package j$.util.stream;

import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final /* synthetic */ class k0 implements Supplier, Consumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ k0(int i, Object obj, Object obj2) {
        this.a = i;
        this.b = obj;
        this.c = obj2;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public void r(Object obj) {
        switch (this.a) {
            case 4:
                ((b3) this.b).f((Consumer) this.c, obj);
                return;
            case 5:
                if (obj == null) {
                    ((AtomicBoolean) this.b).set(true);
                    return;
                } else {
                    ((ConcurrentHashMap) this.c).putIfAbsent(obj, Boolean.TRUE);
                    return;
                }
            default:
                ((BiConsumer) this.b).accept(this.c, obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 4:
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
            case 0:
                return new n0((j$.util.function.Z) this.c, (q0) this.b);
            case 1:
                return new m0((j$.util.function.J) this.c, (q0) this.b);
            case 2:
                return new o0((j$.util.function.r) this.c, (q0) this.b);
            default:
                return new l0((Predicate) this.c, (q0) this.b);
        }
    }
}
