package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class K2 extends O2 implements j$.util.function.K {
    /* JADX INFO: Access modifiers changed from: package-private */
    public K2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public K2(int i) {
        super(i);
    }

    @Override // j$.util.function.K
    public void accept(int i) {
        w();
        int i2 = this.b;
        this.b = i2 + 1;
        ((int[]) this.e)[i2] = i;
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.K) {
            d((j$.util.function.K) consumer);
        } else if (F3.a) {
            F3.a(getClass(), "{0} calling SpinedBuffer.OfInt.forEach(Consumer)");
            throw null;
        } else {
            spliterator().forEachRemaining(consumer);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.g(spliterator());
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }

    @Override // j$.util.stream.O2
    public final Object newArray(int i) {
        return new int[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final void r(Object obj, int i, int i2, Object obj2) {
        int[] iArr = (int[]) obj;
        j$.util.function.K k = (j$.util.function.K) obj2;
        while (i < i2) {
            k.accept(iArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.O2
    public final int s(Object obj) {
        return ((int[]) obj).length;
    }

    public final String toString() {
        int[] iArr = (int[]) b();
        return iArr.length < 200 ? String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(iArr.length), Integer.valueOf(this.c), Arrays.toString(iArr)) : String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(iArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(iArr, 200)));
    }

    @Override // j$.util.stream.O2
    protected final Object[] v() {
        return new int[8];
    }

    @Override // j$.util.stream.O2, java.lang.Iterable
    /* renamed from: x */
    public j$.util.H spliterator() {
        return new J2(this, 0, this.c, 0, this.b);
    }
}
