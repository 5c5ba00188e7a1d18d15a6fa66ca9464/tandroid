package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Iterator;
import org.telegram.messenger.NotificationCenter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class L2 extends N2 implements j$.util.function.W {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L2() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public L2(int i) {
        super(i);
    }

    @Override // j$.util.stream.N2, java.lang.Iterable
    /* renamed from: A */
    public j$.util.K spliterator() {
        return new K2(this, 0, this.c, 0, this.b);
    }

    @Override // j$.util.function.W
    public void accept(long j) {
        z();
        int i = this.b;
        this.b = i + 1;
        ((long[]) this.e)[i] = j;
    }

    @Override // j$.util.stream.N2
    public final Object c(int i) {
        return new long[i];
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    public final void forEach(Consumer consumer) {
        if (consumer instanceof j$.util.function.W) {
            g((j$.util.function.W) consumer);
        } else if (A3.a) {
            A3.a(getClass(), "{0} calling SpinedBuffer.OfLong.forEach(Consumer)");
            throw null;
        } else {
            j$.util.a.h((K2) spliterator(), consumer);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.h(spliterator());
    }

    public final String toString() {
        long[] jArr = (long[]) e();
        if (jArr.length < 200) {
            return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(jArr.length), Integer.valueOf(this.c), Arrays.toString(jArr));
        }
        return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(jArr.length), Integer.valueOf(this.c), Arrays.toString(Arrays.copyOf(jArr, (int) NotificationCenter.storyQualityUpdate)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final void u(Object obj, int i, int i2, Object obj2) {
        long[] jArr = (long[]) obj;
        j$.util.function.W w = (j$.util.function.W) obj2;
        while (i < i2) {
            w.accept(jArr[i]);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.N2
    public final int v(Object obj) {
        return ((long[]) obj).length;
    }

    @Override // j$.util.stream.N2
    protected final Object[] y() {
        return new long[8];
    }
}
