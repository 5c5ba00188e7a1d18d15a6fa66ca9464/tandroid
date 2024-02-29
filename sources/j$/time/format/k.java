package j$.time.format;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class k implements h {
    static final long[] f = {0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L};
    final j$.time.temporal.l a;
    final int b;
    final int c;
    private final y d;
    final int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(j$.time.temporal.l lVar, int i, int i2, y yVar) {
        this.a = lVar;
        this.b = i;
        this.c = i2;
        this.d = yVar;
        this.e = 0;
    }

    protected k(j$.time.temporal.l lVar, int i, int i2, y yVar, int i3) {
        this.a = lVar;
        this.b = i;
        this.c = i2;
        this.d = yVar;
        this.e = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ y b(k kVar) {
        return kVar.d;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x009b A[LOOP:0: B:34:0x0092->B:36:0x009b, LOOP_END] */
    @Override // j$.time.format.h
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean a(t tVar, StringBuilder sb) {
        char c;
        int i;
        Long e = tVar.e(this.a);
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        w b = tVar.b();
        String l = longValue == Long.MIN_VALUE ? "9223372036854775808" : Long.toString(Math.abs(longValue));
        if (l.length() > this.c) {
            throw new j$.time.c("Field " + this.a + " cannot be printed as the value " + longValue + " exceeds the maximum print width of " + this.c);
        }
        b.a(l);
        int i2 = (longValue > 0L ? 1 : (longValue == 0L ? 0 : -1));
        int[] iArr = e.a;
        int ordinal = this.d.ordinal();
        if (i2 >= 0) {
            int i3 = iArr[ordinal];
            if (i3 == 1 ? !((i = this.b) >= 19 || longValue < f[i]) : i3 == 2) {
                c = b.d();
                sb.append(c);
            }
            for (int i4 = 0; i4 < this.b - l.length(); i4++) {
                sb.append(b.e());
            }
            sb.append(l);
            return true;
        }
        int i5 = iArr[ordinal];
        if (i5 == 1 || i5 == 2 || i5 == 3) {
            c = b.c();
            sb.append(c);
            while (i4 < this.b - l.length()) {
            }
            sb.append(l);
            return true;
        }
        if (i5 == 4) {
            throw new j$.time.c("Field " + this.a + " cannot be printed as the value " + longValue + " cannot be negative according to the SignStyle");
        }
        while (i4 < this.b - l.length()) {
        }
        sb.append(l);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k c() {
        return this.e == -1 ? this : new k(this.a, this.b, this.c, this.d, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k d(int i) {
        return new k(this.a, this.b, this.c, this.d, this.e + i);
    }

    public String toString() {
        StringBuilder sb;
        Object obj;
        int i = this.b;
        if (i == 1 && this.c == 19 && this.d == y.NORMAL) {
            sb = new StringBuilder();
            sb.append("Value(");
            obj = this.a;
        } else if (i == this.c && this.d == y.NOT_NEGATIVE) {
            sb = new StringBuilder();
            sb.append("Value(");
            sb.append(this.a);
            sb.append(",");
            sb.append(this.b);
            sb.append(")");
            return sb.toString();
        } else {
            sb = new StringBuilder();
            sb.append("Value(");
            sb.append(this.a);
            sb.append(",");
            sb.append(this.b);
            sb.append(",");
            sb.append(this.c);
            sb.append(",");
            obj = this.d;
        }
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
}
