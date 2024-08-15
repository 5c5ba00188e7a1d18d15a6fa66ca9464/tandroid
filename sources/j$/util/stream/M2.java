package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class M2 extends O2 implements j$.util.function.h0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public M2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public M2(int i) {
        super(i);
    }

    @Override // j$.util.function.h0
    public void accept(long j) {
        w();
        int i = this.b;
        this.b = i + 1;
        ((long[]) this.e)[i] = j;
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.h0) {
            d((j$.util.function.h0) consumer);
        } else if (F3.a) {
            F3.a(getClass(), "{0} calling SpinedBuffer.OfLong.forEach(Consumer)");
            throw null;
        } else {
            spliterator().forEachRemaining(consumer);
        }
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.h(spliterator());
    }

    @Override // j$.util.stream.O2
    public final Object newArray(int i) {
        return new long[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final void r(Object obj, int i, int i2, Object obj2) {
        long[] jArr = (long[]) obj;
        j$.util.function.h0 h0Var = (j$.util.function.h0) obj2;
        while (i < i2) {
            h0Var.accept(jArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final int s(Object obj) {
        return ((long[]) obj).length;
    }

    public final String toString() {
        long[] jArr = (long[]) b();
        return jArr.length < 200 ? String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(jArr.length), Integer.valueOf(this.c), Arrays.toString(jArr)) : String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(jArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(jArr, 200)));
    }

    @Override // j$.util.stream.O2
    protected final Object[] v() {
        return new long[8];
    }

    @Override // j$.util.stream.O2, java.lang.Iterable
    /* renamed from: x */
    public j$.util.K spliterator() {
        return new L2(this, 0, this.c, 0, this.b);
    }
}
