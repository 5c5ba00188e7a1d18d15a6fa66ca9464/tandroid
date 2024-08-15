package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class q {
    m[] a;
    m b = null;
    p c;
    p d;
    int e;
    int f;
    int g;
    final int h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(m[] mVarArr, int i, int i2, int i3) {
        this.a = mVarArr;
        this.h = i;
        this.e = i2;
        this.f = i2;
        this.g = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final m f() {
        m[] mVarArr;
        int length;
        int i;
        p pVar;
        m mVar = this.b;
        if (mVar != null) {
            mVar = mVar.d;
        }
        while (mVar == null) {
            if (this.f >= this.g || (mVarArr = this.a) == null || (length = mVarArr.length) <= (i = this.e) || i < 0) {
                this.b = null;
                return null;
            }
            m m = ConcurrentHashMap.m(mVarArr, i);
            if (m == null || m.a >= 0) {
                mVar = m;
            } else if (m instanceof h) {
                this.a = ((h) m).e;
                p pVar2 = this.d;
                if (pVar2 != null) {
                    this.d = pVar2.d;
                } else {
                    pVar2 = new p();
                }
                pVar2.c = mVarArr;
                pVar2.a = length;
                pVar2.b = i;
                pVar2.d = this.c;
                this.c = pVar2;
                mVar = null;
            } else {
                mVar = m instanceof r ? ((r) m).f : null;
            }
            if (this.c != null) {
                while (true) {
                    pVar = this.c;
                    if (pVar == null) {
                        break;
                    }
                    int i2 = this.e;
                    int i3 = pVar.a;
                    int i4 = i2 + i3;
                    this.e = i4;
                    if (i4 < length) {
                        break;
                    }
                    this.e = pVar.b;
                    this.a = pVar.c;
                    pVar.c = null;
                    p pVar3 = pVar.d;
                    pVar.d = this.d;
                    this.c = pVar3;
                    this.d = pVar;
                    length = i3;
                }
                if (pVar == null) {
                    int i5 = this.e + this.h;
                    this.e = i5;
                    if (i5 >= length) {
                        int i6 = this.f + 1;
                        this.f = i6;
                        this.e = i6;
                    }
                }
            } else {
                int i7 = i + this.h;
                this.e = i7;
                if (i7 >= length) {
                    int i8 = this.f + 1;
                    this.f = i8;
                    this.e = i8;
                }
            }
        }
        this.b = mVar;
        return mVar;
    }
}
