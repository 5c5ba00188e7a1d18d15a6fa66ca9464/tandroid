package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class I2 extends O2 implements j$.util.function.m {
    /* JADX INFO: Access modifiers changed from: package-private */
    public I2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public I2(int i) {
        super(i);
    }

    @Override // j$.util.function.m
    public void accept(double d) {
        w();
        int i = this.b;
        this.b = i + 1;
        ((double[]) this.e)[i] = d;
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.m) {
            d((j$.util.function.m) consumer);
        } else if (F3.a) {
            F3.a(getClass(), "{0} calling SpinedBuffer.OfDouble.forEach(Consumer)");
            throw null;
        } else {
            spliterator().forEachRemaining(consumer);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.f(spliterator());
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.O2
    public final Object newArray(int i) {
        return new double[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final void r(Object obj, int i, int i2, Object obj2) {
        double[] dArr = (double[]) obj;
        j$.util.function.m mVar = (j$.util.function.m) obj2;
        while (i < i2) {
            mVar.accept(dArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final int s(Object obj) {
        return ((double[]) obj).length;
    }

    public final String toString() {
        double[] dArr = (double[]) b();
        return dArr.length < 200 ? String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(dArr.length), Integer.valueOf(this.c), Arrays.toString(dArr)) : String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(dArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(dArr, 200)));
    }

    @Override // j$.util.stream.O2
    protected final Object[] v() {
        return new double[8];
    }

    @Override // j$.util.stream.O2, java.lang.Iterable
    /* renamed from: x */
    public j$.util.E spliterator() {
        return new H2(this, 0, this.c, 0, this.b);
    }
}
