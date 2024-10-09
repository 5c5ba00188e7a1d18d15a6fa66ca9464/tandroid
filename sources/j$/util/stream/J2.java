package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
import org.telegram.messenger.NotificationCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class J2 extends N2 implements j$.util.function.F {
    /* JADX INFO: Access modifiers changed from: package-private */
    public J2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public J2(int i) {
        super(i);
    }

    @Override // j$.util.stream.N2, java.lang.Iterable
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public j$.util.H spliterator() {
        return new I2(this, 0, this.c, 0, this.b);
    }

    @Override // j$.util.function.F
    public void accept(int i) {
        z();
        int[] iArr = (int[]) this.e;
        int i2 = this.b;
        this.b = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.stream.N2
    public final Object c(int i) {
        return new int[i];
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.F) {
            g((j$.util.function.F) consumer);
        } else {
            if (A3.a) {
                A3.a(getClass(), "{0} calling SpinedBuffer.OfInt.forEach(Consumer)");
                throw null;
            }
            j$.util.a.f((I2) spliterator(), consumer);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.g(spliterator());
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    public final String toString() {
        int[] iArr = (int[]) e();
        if (iArr.length < 200) {
            return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(iArr.length), Integer.valueOf(this.c), Arrays.toString(iArr));
        }
        return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(iArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(iArr, NotificationCenter.storyQualityUpdate)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final void u(Object obj, int i, int i2, Object obj2) {
        int[] iArr = (int[]) obj;
        j$.util.function.F f = (j$.util.function.F) obj2;
        while (i < i2) {
            f.accept(iArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final int v(Object obj) {
        return ((int[]) obj).length;
    }

    @Override // j$.util.stream.N2
    protected final Object[] y() {
        return new int[8];
    }
}
