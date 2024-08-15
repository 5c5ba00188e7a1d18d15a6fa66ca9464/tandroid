package j$.util.stream;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.LongFunction;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import j$.util.function.ToLongFunction;
import java.util.Collection;
import java.util.LinkedHashSet;
/* loaded from: classes2.dex */
public final /* synthetic */ class J0 implements LongFunction, j$.util.function.N, ToLongFunction, Consumer, BiConsumer, Supplier, j$.util.function.i, j$.util.function.p, j$.util.function.z0, Predicate, j$.util.function.G, j$.util.function.C0 {
    public final /* synthetic */ int a;

    public /* synthetic */ J0(int i) {
        this.a = i;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
    }

    @Override // j$.util.function.z0
    public final void accept(Object obj, double d) {
        ((j$.util.h) obj).accept(d);
    }

    @Override // j$.util.function.C0
    public final void accept(Object obj, int i) {
        ((j$.util.i) obj).accept(i);
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 9:
                ((Collection) obj).add(obj2);
                return;
            case 14:
                ((LinkedHashSet) obj).add(obj2);
                return;
            case 15:
                ((LinkedHashSet) obj).addAll((LinkedHashSet) obj2);
                return;
            case 20:
                ((j$.util.h) obj).a((j$.util.h) obj2);
                return;
            default:
                ((j$.util.i) obj).a((j$.util.i) obj2);
                return;
        }
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate and(Predicate predicate) {
        switch (this.a) {
            case 21:
                return Predicate.-CC.$default$and(this, predicate);
            case 22:
                return Predicate.-CC.$default$and(this, predicate);
            case 23:
                return Predicate.-CC.$default$and(this, predicate);
            default:
                return Predicate.-CC.$default$and(this, predicate);
        }
    }

    @Override // j$.util.function.BiConsumer
    public final /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 9:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 14:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 15:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 20:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            default:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
        }
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 7:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.p
    public final Object apply(double d) {
        return Double.valueOf(d);
    }

    @Override // j$.util.function.N
    public final Object apply(int i) {
        switch (this.a) {
            case 1:
                return new Object[i];
            case 2:
            default:
                return Integer.valueOf(i);
            case 3:
                return new Object[i];
            case 4:
                return new Integer[i];
            case 5:
                return new Long[i];
            case 6:
                return new Double[i];
        }
    }

    @Override // j$.util.function.LongFunction
    public final Object apply(long j) {
        return u1.t(j);
    }

    @Override // j$.util.function.i
    public final double applyAsDouble(double d, double d2) {
        switch (this.a) {
            case 16:
                return Math.min(d, d2);
            default:
                return Math.max(d, d2);
        }
    }

    @Override // j$.util.function.G
    public final int applyAsInt(int i, int i2) {
        switch (this.a) {
            case 25:
                return Math.min(i, i2);
            default:
                return i + i2;
        }
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        return 1L;
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 10:
                return new j$.util.h();
            case 11:
                return new j$.util.i();
            case 12:
                return new j$.util.j();
            default:
                return new LinkedHashSet();
        }
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate negate() {
        switch (this.a) {
            case 21:
                return Predicate.-CC.$default$negate(this);
            case 22:
                return Predicate.-CC.$default$negate(this);
            case 23:
                return Predicate.-CC.$default$negate(this);
            default:
                return Predicate.-CC.$default$negate(this);
        }
    }

    @Override // j$.util.function.Predicate
    public final /* synthetic */ Predicate or(Predicate predicate) {
        switch (this.a) {
            case 21:
                return Predicate.-CC.$default$or(this, predicate);
            case 22:
                return Predicate.-CC.$default$or(this, predicate);
            case 23:
                return Predicate.-CC.$default$or(this, predicate);
            default:
                return Predicate.-CC.$default$or(this, predicate);
        }
    }

    @Override // j$.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.a) {
            case 21:
                return ((j$.util.l) obj).c();
            case 22:
                return ((j$.util.n) obj).c();
            case 23:
                return ((Optional) obj).isPresent();
            default:
                return ((j$.util.m) obj).c();
        }
    }
}
