package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class b implements j$.util.function.N, Function, j$.util.function.z0, BiConsumer, j$.util.function.y, Supplier, ToDoubleFunction, ToIntFunction, j$.util.function.X, j$.util.function.C0, j$.util.function.w0, ToLongFunction, j$.util.function.F0, LongFunction {
    public final /* synthetic */ int a;

    public /* synthetic */ b(int i) {
        this.a = i;
    }

    @Override // j$.util.function.w0
    public final j$.util.function.w0 a(j$.util.function.w0 w0Var) {
        w0Var.getClass();
        return new j$.util.function.t0(this, w0Var, 1);
    }

    @Override // j$.util.function.z0
    public final void accept(Object obj, double d) {
        switch (this.a) {
            case 3:
                double[] dArr = (double[]) obj;
                Collectors.a(dArr, d);
                dArr[2] = dArr[2] + d;
                return;
            default:
                double[] dArr2 = (double[]) obj;
                dArr2[2] = dArr2[2] + 1.0d;
                Collectors.a(dArr2, d);
                dArr2[3] = dArr2[3] + d;
                return;
        }
    }

    @Override // j$.util.function.C0
    public final void accept(Object obj, int i) {
        long[] jArr = (long[]) obj;
        jArr[0] = jArr[0] + 1;
        jArr[1] = jArr[1] + i;
    }

    @Override // j$.util.function.F0
    public final void accept(Object obj, long j) {
        long[] jArr = (long[]) obj;
        jArr[0] = jArr[0] + 1;
        jArr[1] = jArr[1] + j;
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 4:
                double[] dArr = (double[]) obj;
                double[] dArr2 = (double[]) obj2;
                Collectors.a(dArr, dArr2[0]);
                Collectors.a(dArr, dArr2[1]);
                dArr[2] = dArr[2] + dArr2[2];
                return;
            case 8:
                double[] dArr3 = (double[]) obj;
                double[] dArr4 = (double[]) obj2;
                Collectors.a(dArr3, dArr4[0]);
                Collectors.a(dArr3, dArr4[1]);
                dArr3[2] = dArr3[2] + dArr4[2];
                dArr3[3] = dArr3[3] + dArr4[3];
                return;
            case 20:
                long[] jArr = (long[]) obj;
                long[] jArr2 = (long[]) obj2;
                jArr[0] = jArr[0] + jArr2[0];
                jArr[1] = jArr[1] + jArr2[1];
                return;
            default:
                long[] jArr3 = (long[]) obj;
                long[] jArr4 = (long[]) obj2;
                jArr3[0] = jArr3[0] + jArr4[0];
                jArr3[1] = jArr3[1] + jArr4[1];
                return;
        }
    }

    @Override // j$.util.function.BiConsumer
    public final /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 4:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 8:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 20:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            default:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
        }
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function andThen(Function function) {
        return Function.-CC.$default$andThen(this, function);
    }

    @Override // j$.util.function.N
    public final Object apply(int i) {
        switch (this.a) {
            case 0:
                return new Object[i];
            case 2:
                return new Double[i];
            case 15:
                int i2 = T.h;
                return new Object[i];
            case 21:
                return new Integer[i];
            default:
                return new Long[i];
        }
    }

    @Override // j$.util.function.LongFunction
    public final Object apply(long j) {
        switch (this.a) {
            case 28:
                return u1.m(j);
            default:
                return u1.s(j);
        }
    }

    @Override // j$.util.function.Function
    public final Object apply(Object obj) {
        Set set = Collectors.a;
        return obj;
    }

    @Override // j$.util.function.ToDoubleFunction
    public final double applyAsDouble(Object obj) {
        return ((Double) obj).doubleValue();
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((Integer) obj).intValue();
    }

    @Override // j$.util.function.y
    public final long applyAsLong(double d) {
        return 1L;
    }

    @Override // j$.util.function.X
    public final long applyAsLong(int i) {
        return 1L;
    }

    @Override // j$.util.function.w0
    public final long applyAsLong(long j) {
        return 1L;
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        return ((Long) obj).longValue();
    }

    @Override // j$.util.function.w0
    public final j$.util.function.w0 b(j$.util.function.w0 w0Var) {
        w0Var.getClass();
        return new j$.util.function.t0(this, w0Var, 0);
    }

    @Override // j$.util.function.Function
    public final /* synthetic */ Function compose(Function function) {
        return Function.-CC.$default$compose(this, function);
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 6:
                return new double[4];
            case 7:
            case 8:
            case 9:
            case 15:
            case 16:
            case 17:
            default:
                return new long[2];
            case 10:
                return new double[3];
            case 11:
                return new H();
            case 12:
                return new J();
            case 13:
                return new K();
            case 14:
                return new I();
            case 18:
                return new long[2];
        }
    }
}
