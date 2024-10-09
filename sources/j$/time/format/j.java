package j$.time.format;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class j implements g {
    static final long[] f = {0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L};
    final j$.time.temporal.l a;
    final int b;
    final int c;
    private final y d;
    final int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(j$.time.temporal.l lVar, int i, int i2, y yVar) {
        this.a = lVar;
        this.b = i;
        this.c = i2;
        this.d = yVar;
        this.e = 0;
    }

    protected j(j$.time.temporal.l lVar, int i, int i2, y yVar, int i3) {
        this.a = lVar;
        this.b = i;
        this.c = i2;
        this.d = yVar;
        this.e = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ y b(j jVar) {
        return jVar.d;
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        j$.time.temporal.l lVar = this.a;
        Long e = sVar.e(lVar);
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        w b = sVar.b();
        String l = longValue == Long.MIN_VALUE ? "9223372036854775808" : Long.toString(Math.abs(longValue));
        int length = l.length();
        int i = this.c;
        if (length > i) {
            throw new j$.time.c("Field " + lVar + " cannot be printed as the value " + longValue + " exceeds the maximum print width of " + i);
        }
        b.getClass();
        int i2 = this.b;
        y yVar = this.d;
        if (longValue >= 0) {
            int i3 = d.a[yVar.ordinal()];
            if (i3 == 1 ? !(i2 >= 19 || longValue < f[i2]) : i3 == 2) {
                sb.append('+');
            }
        } else {
            int i4 = d.a[yVar.ordinal()];
            if (i4 == 1 || i4 == 2 || i4 == 3) {
                sb.append('-');
            } else if (i4 == 4) {
                throw new j$.time.c("Field " + lVar + " cannot be printed as the value " + longValue + " cannot be negative according to the SignStyle");
            }
        }
        for (int i5 = 0; i5 < i2 - l.length(); i5++) {
            sb.append('0');
        }
        sb.append(l);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j c() {
        if (this.e == -1) {
            return this;
        }
        return new j(this.a, this.b, this.c, this.d, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j d(int i) {
        return new j(this.a, this.b, this.c, this.d, this.e + i);
    }

    public final String toString() {
        j$.time.temporal.l lVar = this.a;
        y yVar = this.d;
        int i = this.c;
        int i2 = this.b;
        if (i2 == 1 && i == 19 && yVar == y.NORMAL) {
            return "Value(" + lVar + ")";
        }
        if (i2 == i && yVar == y.NOT_NEGATIVE) {
            return "Value(" + lVar + "," + i2 + ")";
        }
        return "Value(" + lVar + "," + i2 + "," + i + "," + yVar + ")";
    }
}
