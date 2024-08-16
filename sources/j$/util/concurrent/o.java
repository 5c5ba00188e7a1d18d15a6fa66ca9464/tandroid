package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class o {
    k[] a;
    k b = null;
    n c;
    n d;
    int e;
    int f;
    int g;
    final int h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(k[] kVarArr, int i, int i2, int i3) {
        this.a = kVarArr;
        this.h = i;
        this.e = i2;
        this.f = i2;
        this.g = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final k b() {
        k[] kVarArr;
        int length;
        int i;
        n nVar;
        k kVar = this.b;
        if (kVar != null) {
            kVar = kVar.d;
        }
        while (kVar == null) {
            if (this.f >= this.g || (kVarArr = this.a) == null || (length = kVarArr.length) <= (i = this.e) || i < 0) {
                this.b = null;
                return null;
            }
            k l = ConcurrentHashMap.l(kVarArr, i);
            if (l == null || l.a >= 0) {
                kVar = l;
            } else if (l instanceof f) {
                this.a = ((f) l).e;
                n nVar2 = this.d;
                if (nVar2 != null) {
                    this.d = nVar2.d;
                } else {
                    nVar2 = new n();
                }
                nVar2.c = kVarArr;
                nVar2.a = length;
                nVar2.b = i;
                nVar2.d = this.c;
                this.c = nVar2;
                kVar = null;
            } else {
                kVar = l instanceof p ? ((p) l).f : null;
            }
            if (this.c != null) {
                while (true) {
                    nVar = this.c;
                    if (nVar == null) {
                        break;
                    }
                    int i2 = this.e;
                    int i3 = nVar.a;
                    int i4 = i2 + i3;
                    this.e = i4;
                    if (i4 < length) {
                        break;
                    }
                    this.e = nVar.b;
                    this.a = nVar.c;
                    nVar.c = null;
                    n nVar3 = nVar.d;
                    nVar.d = this.d;
                    this.c = nVar3;
                    this.d = nVar;
                    length = i3;
                }
                if (nVar == null) {
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
        this.b = kVar;
        return kVar;
    }
}
