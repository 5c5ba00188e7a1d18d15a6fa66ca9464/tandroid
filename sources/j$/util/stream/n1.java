package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n1 extends q1 implements c2 {
    private final int[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n1(j$.util.Q q, b bVar, int[] iArr) {
        super(iArr.length, q, bVar);
        this.h = iArr;
    }

    n1(n1 n1Var, j$.util.Q q, long j, long j2) {
        super(n1Var, q, j, j2, n1Var.h.length);
        this.h = n1Var.h;
    }

    @Override // j$.util.stream.q1
    final q1 a(j$.util.Q q, long j, long j2) {
        return new n1(this, q, j, j2);
    }

    @Override // j$.util.stream.q1, j$.util.stream.e2
    public final void accept(int i) {
        int i2 = this.f;
        if (i2 >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        int[] iArr = this.h;
        this.f = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        o((Integer) obj);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }
}
