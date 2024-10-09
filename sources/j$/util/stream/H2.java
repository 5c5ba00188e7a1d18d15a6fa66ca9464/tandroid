package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
import org.telegram.messenger.NotificationCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class H2 extends N2 implements j$.util.function.n {
    /* JADX INFO: Access modifiers changed from: package-private */
    public H2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public H2(int i) {
        super(i);
    }

    @Override // j$.util.stream.N2, java.lang.Iterable
    /* renamed from: A, reason: merged with bridge method [inline-methods] */
    public j$.util.E spliterator() {
        return new G2(this, 0, this.c, 0, this.b);
    }

    @Override // j$.util.function.n
    public void accept(double d) {
        z();
        double[] dArr = (double[]) this.e;
        int i = this.b;
        this.b = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.N2
    public final Object c(int i) {
        return new double[i];
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.n) {
            g((j$.util.function.n) consumer);
        } else {
            if (A3.a) {
                A3.a(getClass(), "{0} calling SpinedBuffer.OfDouble.forEach(Consumer)");
                throw null;
            }
            j$.util.a.b((G2) spliterator(), consumer);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.f(spliterator());
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    public final String toString() {
        double[] dArr = (double[]) e();
        if (dArr.length < 200) {
            return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(dArr.length), Integer.valueOf(this.c), Arrays.toString(dArr));
        }
        return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(dArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(dArr, NotificationCenter.storyQualityUpdate)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final void u(Object obj, int i, int i2, Object obj2) {
        double[] dArr = (double[]) obj;
        j$.util.function.n nVar = (j$.util.function.n) obj2;
        while (i < i2) {
            nVar.accept(dArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final int v(Object obj) {
        return ((double[]) obj).length;
    }

    @Override // j$.util.stream.N2
    protected final Object[] y() {
        return new double[8];
    }
}
