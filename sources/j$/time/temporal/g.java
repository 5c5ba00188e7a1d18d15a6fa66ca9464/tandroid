package j$.time.temporal;

import j$.time.DayOfWeek;
import j$.time.LocalDate;
/* loaded from: classes2.dex */
enum g implements l {
    DAY_OF_QUARTER { // from class: j$.time.temporal.c
        @Override // j$.time.temporal.l
        public x a() {
            return x.j(1L, 90L, 92L);
        }

        @Override // j$.time.temporal.l
        public long c(k kVar) {
            int[] iArr;
            if (d(kVar)) {
                int a = kVar.a(a.DAY_OF_YEAR);
                int a2 = kVar.a(a.MONTH_OF_YEAR);
                long c = kVar.c(a.YEAR);
                iArr = g.a;
                return a - iArr[((a2 - 1) / 3) + (j$.time.chrono.h.a.a(c) ? 4 : 0)];
            }
            throw new w("Unsupported field: DayOfQuarter");
        }

        @Override // j$.time.temporal.l
        public boolean d(k kVar) {
            return kVar.e(a.DAY_OF_YEAR) && kVar.e(a.MONTH_OF_YEAR) && kVar.e(a.YEAR) && g.g(kVar);
        }

        @Override // j$.time.temporal.g, j$.time.temporal.l
        public x e(k kVar) {
            if (d(kVar)) {
                long c = kVar.c(g.QUARTER_OF_YEAR);
                if (c == 1) {
                    return j$.time.chrono.h.a.a(kVar.c(a.YEAR)) ? x.i(1L, 91L) : x.i(1L, 90L);
                }
                return c == 2 ? x.i(1L, 91L) : (c == 3 || c == 4) ? x.i(1L, 92L) : a();
            }
            throw new w("Unsupported field: DayOfQuarter");
        }

        @Override // java.lang.Enum
        public String toString() {
            return "DayOfQuarter";
        }
    },
    QUARTER_OF_YEAR { // from class: j$.time.temporal.d
        @Override // j$.time.temporal.l
        public x a() {
            return x.i(1L, 4L);
        }

        @Override // j$.time.temporal.l
        public long c(k kVar) {
            if (d(kVar)) {
                return (kVar.c(a.MONTH_OF_YEAR) + 2) / 3;
            }
            throw new w("Unsupported field: QuarterOfYear");
        }

        @Override // j$.time.temporal.l
        public boolean d(k kVar) {
            return kVar.e(a.MONTH_OF_YEAR) && g.g(kVar);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "QuarterOfYear";
        }
    },
    WEEK_OF_WEEK_BASED_YEAR { // from class: j$.time.temporal.e
        @Override // j$.time.temporal.l
        public x a() {
            return x.j(1L, 52L, 53L);
        }

        @Override // j$.time.temporal.l
        public long c(k kVar) {
            if (d(kVar)) {
                return g.j(LocalDate.h(kVar));
            }
            throw new w("Unsupported field: WeekOfWeekBasedYear");
        }

        @Override // j$.time.temporal.l
        public boolean d(k kVar) {
            return kVar.e(a.EPOCH_DAY) && g.g(kVar);
        }

        @Override // j$.time.temporal.g, j$.time.temporal.l
        public x e(k kVar) {
            x m;
            if (d(kVar)) {
                m = g.m(LocalDate.h(kVar));
                return m;
            }
            throw new w("Unsupported field: WeekOfWeekBasedYear");
        }

        @Override // java.lang.Enum
        public String toString() {
            return "WeekOfWeekBasedYear";
        }
    },
    WEEK_BASED_YEAR { // from class: j$.time.temporal.f
        @Override // j$.time.temporal.l
        public x a() {
            return a.YEAR.a();
        }

        @Override // j$.time.temporal.l
        public long c(k kVar) {
            int l;
            if (d(kVar)) {
                l = g.l(LocalDate.h(kVar));
                return l;
            }
            throw new w("Unsupported field: WeekBasedYear");
        }

        @Override // j$.time.temporal.l
        public boolean d(k kVar) {
            return kVar.e(a.EPOCH_DAY) && g.g(kVar);
        }

        @Override // java.lang.Enum
        public String toString() {
            return "WeekBasedYear";
        }
    };
    
    private static final int[] a = {0, 90, 181, 273, 0, 91, 182, 274};

    /* JADX INFO: Access modifiers changed from: package-private */
    g(j jVar) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g(k kVar) {
        int i = t.a;
        Object obj = (j$.time.chrono.g) kVar.d(n.a);
        if (obj == null) {
            obj = j$.time.chrono.h.a;
        }
        return ((j$.time.chrono.a) obj).equals(j$.time.chrono.h.a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004b, code lost:
        if ((r0 == -3 || (r0 == -2 && r5.n())) == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int j(LocalDate localDate) {
        int ordinal = localDate.j().ordinal();
        int i = 1;
        int k = localDate.k() - 1;
        int i2 = (3 - ordinal) + k;
        int i3 = (i2 - ((i2 / 7) * 7)) - 3;
        if (i3 < -3) {
            i3 += 7;
        }
        if (k < i3) {
            return (int) m(localDate.u(180).q(-1L)).d();
        }
        int i4 = ((k - i3) / 7) + 1;
        if (i4 == 53) {
        }
        i = i4;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int l(LocalDate localDate) {
        int m = localDate.m();
        int k = localDate.k();
        if (k <= 3) {
            return k - localDate.j().ordinal() < -2 ? m - 1 : m;
        } else if (k >= 363) {
            return ((k - 363) - (localDate.n() ? 1 : 0)) - localDate.j().ordinal() >= 0 ? m + 1 : m;
        } else {
            return m;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static x m(LocalDate localDate) {
        LocalDate of = LocalDate.of(l(localDate), 1, 1);
        return x.i(1L, (of.j() == DayOfWeek.THURSDAY || (of.j() == DayOfWeek.WEDNESDAY && of.n())) ? 53 : 52);
    }

    @Override // j$.time.temporal.l
    public boolean b() {
        return false;
    }

    public x e(k kVar) {
        return a();
    }

    @Override // j$.time.temporal.l
    public boolean f() {
        return true;
    }
}
