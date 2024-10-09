package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public final /* synthetic */ class l implements Function, j$.util.function.i0, BiConsumer, Supplier, LongFunction, j$.util.function.I, j$.util.function.j, j$.util.function.q, j$.util.function.v, ToDoubleFunction {
    public final /* synthetic */ int a;

    public /* synthetic */ l(int i) {
        this.a = i;
    }

    @Override // j$.util.function.i0
    public void accept(Object obj, double d) {
        switch (this.a) {
            case 1:
                double[] dArr = (double[]) obj;
                Collectors.a(dArr, d);
                dArr[2] = dArr[2] + d;
                return;
            case 2:
            default:
                ((j$.util.h) obj).accept(d);
                return;
            case 3:
                double[] dArr2 = (double[]) obj;
                dArr2[2] = dArr2[2] + 1.0d;
                Collectors.a(dArr2, d);
                dArr2[3] = dArr2[3] + d;
                return;
        }
    }

    @Override // j$.util.function.BiConsumer
    public void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 2:
                double[] dArr = (double[]) obj;
                double[] dArr2 = (double[]) obj2;
                Collectors.a(dArr, dArr2[0]);
                Collectors.a(dArr, dArr2[1]);
                dArr[2] = dArr[2] + dArr2[2];
                return;
            case 4:
                double[] dArr3 = (double[]) obj;
                double[] dArr4 = (double[]) obj2;
                Collectors.a(dArr3, dArr4[0]);
                Collectors.a(dArr3, dArr4[1]);
                dArr3[2] = dArr3[2] + dArr4[2];
                dArr3[3] = dArr3[3] + dArr4[3];
                return;
            case 14:
                ((Collection) obj).add(obj2);
                return;
            case 18:
                ((LinkedHashSet) obj).add(obj2);
                return;
            case 19:
                ((LinkedHashSet) obj).addAll((LinkedHashSet) obj2);
                return;
            default:
                ((j$.util.h) obj).a((j$.util.h) obj2);
                return;
        }
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 2:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 4:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 14:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 18:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            case 19:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            default:
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
        }
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function andThen(Function function) {
        return Function.-CC.$default$andThen(this, function);
    }

    @Override // j$.util.function.q
    public Object apply(double d) {
        return Double.valueOf(d);
    }

    @Override // j$.util.function.I
    public Object apply(int i) {
        switch (this.a) {
            case 12:
                return new Object[i];
            default:
                return new Double[i];
        }
    }

    @Override // j$.util.function.LongFunction
    public Object apply(long j) {
        switch (this.a) {
            case 9:
                return t0.J(j);
            case 10:
                return t0.R(j);
            default:
                return t0.T(j);
        }
    }

    @Override // j$.util.function.Function
    public Object apply(Object obj) {
        Set set = Collectors.a;
        return obj;
    }

    @Override // j$.util.function.j
    public double applyAsDouble(double d, double d2) {
        switch (this.a) {
            case 20:
                return Math.min(d, d2);
            default:
                return Math.max(d, d2);
        }
    }

    @Override // j$.util.function.ToDoubleFunction
    public double applyAsDouble(Object obj) {
        return ((Double) obj).doubleValue();
    }

    @Override // j$.util.function.v
    public long applyAsLong(double d) {
        return 1L;
    }

    @Override // j$.util.function.Function
    public /* synthetic */ Function compose(Function function) {
        return Function.-CC.$default$compose(this, function);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        switch (this.a) {
            case 5:
                return new J();
            case 6:
                return new G();
            case 7:
                return new I();
            case 8:
                return new H();
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 18:
            case 19:
            case 20:
            default:
                return new double[3];
            case 13:
                return new j$.util.h();
            case 15:
                return new j$.util.i();
            case 16:
                return new j$.util.k();
            case 17:
                return new LinkedHashSet();
            case 21:
                return new double[4];
        }
    }
}
