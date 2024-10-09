package j$.util.stream;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.LongFunction;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;

/* loaded from: classes2.dex */
public final /* synthetic */ class E implements Predicate, j$.util.function.I, ToIntFunction, j$.util.function.N, j$.util.function.B, j$.util.function.l0, BiConsumer, Supplier, j$.util.function.o0, j$.util.function.S, ToLongFunction, LongFunction, j$.util.function.f0, Consumer {
    public final /* synthetic */ int a;

    public /* synthetic */ E(int i) {
        this.a = i;
    }

    @Override // j$.util.function.f0
    public j$.util.function.f0 a(j$.util.function.f0 f0Var) {
        f0Var.getClass();
        return new j$.util.function.c0(this, f0Var, 0);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public void r(Object obj) {
    }

    @Override // j$.util.function.l0
    public void accept(Object obj, int i) {
        switch (this.a) {
            case 10:
                ((j$.util.i) obj).accept(i);
                return;
            default:
                long[] jArr = (long[]) obj;
                jArr[0] = jArr[0] + 1;
                jArr[1] = jArr[1] + i;
                return;
        }
    }

    @Override // j$.util.function.o0
    public void accept(Object obj, long j) {
        switch (this.a) {
            case 17:
                ((j$.util.k) obj).accept(j);
                return;
            default:
                long[] jArr = (long[]) obj;
                jArr[0] = jArr[0] + 1;
                jArr[1] = jArr[1] + j;
                return;
        }
    }

    @Override // j$.util.function.BiConsumer
    public void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 11:
                ((j$.util.i) obj).a((j$.util.i) obj2);
                return;
            case 16:
                long[] jArr = (long[]) obj;
                long[] jArr2 = (long[]) obj2;
                jArr[0] = jArr[0] + jArr2[0];
                jArr[1] = jArr[1] + jArr2[1];
                return;
            case 21:
                ((j$.util.k) obj).a((j$.util.k) obj2);
                return;
            default:
                long[] jArr3 = (long[]) obj;
                long[] jArr4 = (long[]) obj2;
                jArr3[0] = jArr3[0] + jArr4[0];
                jArr3[1] = jArr3[1] + jArr4[1];
                return;
        }
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate and(Predicate predicate) {
        switch (this.a) {
            case 0:
                return Predicate.-CC.$default$and(this, predicate);
            case 1:
                return Predicate.-CC.$default$and(this, predicate);
            case 2:
                return Predicate.-CC.$default$and(this, predicate);
            default:
                return Predicate.-CC.$default$and(this, predicate);
        }
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 11:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 16:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 21:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            default:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.I
    public Object apply(int i) {
        switch (this.a) {
            case 4:
                return new Object[i];
            case 5:
                return new Integer[i];
            case 6:
            case 7:
            default:
                return new Long[i];
            case 8:
                return Integer.valueOf(i);
        }
    }

    @Override // j$.util.function.LongFunction
    public Object apply(long j) {
        return Long.valueOf(j);
    }

    @Override // j$.util.function.B
    public int applyAsInt(int i, int i2) {
        switch (this.a) {
            case 9:
                return Math.min(i, i2);
            case 12:
                return i + i2;
            default:
                return Math.max(i, i2);
        }
    }

    @Override // j$.util.function.ToIntFunction
    public int applyAsInt(Object obj) {
        return ((Integer) obj).intValue();
    }

    @Override // j$.util.function.N
    public long applyAsLong(int i) {
        return 1L;
    }

    @Override // j$.util.function.f0
    public long applyAsLong(long j) {
        return 1L;
    }

    @Override // j$.util.function.S
    public long applyAsLong(long j, long j2) {
        switch (this.a) {
            case 18:
                return j + j2;
            case 19:
                return Math.min(j, j2);
            default:
                return Math.max(j, j2);
        }
    }

    @Override // j$.util.function.ToLongFunction
    public long applyAsLong(Object obj) {
        return ((Long) obj).longValue();
    }

    @Override // j$.util.function.f0
    public j$.util.function.f0 c(j$.util.function.f0 f0Var) {
        f0Var.getClass();
        return new j$.util.function.c0(this, f0Var, 1);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        switch (this.a) {
            case 14:
                return new long[2];
            default:
                return new long[2];
        }
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate negate() {
        switch (this.a) {
            case 0:
                return Predicate.-CC.$default$negate(this);
            case 1:
                return Predicate.-CC.$default$negate(this);
            case 2:
                return Predicate.-CC.$default$negate(this);
            default:
                return Predicate.-CC.$default$negate(this);
        }
    }

    @Override // j$.util.function.Predicate
    public /* synthetic */ Predicate or(Predicate predicate) {
        switch (this.a) {
            case 0:
                return Predicate.-CC.$default$or(this, predicate);
            case 1:
                return Predicate.-CC.$default$or(this, predicate);
            case 2:
                return Predicate.-CC.$default$or(this, predicate);
            default:
                return Predicate.-CC.$default$or(this, predicate);
        }
    }

    @Override // j$.util.function.Predicate
    public boolean test(Object obj) {
        switch (this.a) {
            case 0:
                return ((Optional) obj).isPresent();
            case 1:
                return ((j$.util.l) obj).c();
            case 2:
                return ((j$.util.n) obj).c();
            default:
                return ((j$.util.m) obj).c();
        }
    }
}
