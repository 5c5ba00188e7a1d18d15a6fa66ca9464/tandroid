package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class E1 implements B1 {
    final Object[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public E1(long j, j$.util.function.m mVar) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = (Object[]) mVar.apply((int) j);
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public E1(Object[] objArr) {
        this.a = objArr;
        this.b = objArr.length;
    }

    @Override // j$.util.stream.B1
    public B1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.B1
    public long count() {
        return this.b;
    }

    @Override // j$.util.stream.B1
    public void forEach(Consumer consumer) {
        for (int i = 0; i < this.b; i++) {
            consumer.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.B1
    public void i(Object[] objArr, int i) {
        System.arraycopy(this.a, 0, objArr, i, this.b);
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.B1
    public Object[] q(j$.util.function.m mVar) {
        Object[] objArr = this.a;
        if (objArr.length == this.b) {
            return objArr;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ B1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.q(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return j$.util.J.m(this.a, 0, this.b, 1040);
    }

    public String toString() {
        return String.format("ArrayNode[%d][%s]", Integer.valueOf(this.a.length - this.b), Arrays.toString(this.a));
    }
}
