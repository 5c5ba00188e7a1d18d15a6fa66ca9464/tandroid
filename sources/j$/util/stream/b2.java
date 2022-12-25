package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class b2 extends D1 implements s1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b2(long j, j$.util.function.m mVar) {
        super(j, mVar);
    }

    @Override // j$.util.stream.s1
    public A1 a() {
        if (this.b >= this.a.length) {
            return this;
        }
        throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.b), Integer.valueOf(this.a.length)));
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        int i = this.b;
        Object[] objArr = this.a;
        if (i >= objArr.length) {
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.a.length)));
        }
        this.b = i + 1;
        objArr[i] = obj;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
    }

    @Override // j$.util.stream.m3
    public void m() {
        if (this.b < this.a.length) {
            throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.b), Integer.valueOf(this.a.length)));
        }
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        if (j != this.a.length) {
            throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j), Integer.valueOf(this.a.length)));
        }
        this.b = 0;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }

    @Override // j$.util.stream.D1
    public String toString() {
        return String.format("FixedNodeBuilder[%d][%s]", Integer.valueOf(this.a.length - this.b), Arrays.toString(this.a));
    }
}
