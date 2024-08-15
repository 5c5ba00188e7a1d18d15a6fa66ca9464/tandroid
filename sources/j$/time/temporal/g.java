package j$.time.temporal;

import j$.time.DayOfWeek;
import j$.time.LocalDate;
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes2.dex */
abstract class g implements l {
    public static final g DAY_OF_QUARTER;
    public static final g QUARTER_OF_YEAR;
    public static final g WEEK_BASED_YEAR;
    public static final g WEEK_OF_WEEK_BASED_YEAR;
    private static final int[] a;
    private static final /* synthetic */ g[] b;

    static {
        g gVar = new g() { // from class: j$.time.temporal.c
            @Override // j$.time.temporal.l
            public final boolean a(k kVar) {
                return kVar.b(a.DAY_OF_YEAR) && kVar.b(a.MONTH_OF_YEAR) && kVar.b(a.YEAR) && g.e(kVar);
            }

            @Override // j$.time.temporal.g, j$.time.temporal.l
            public final q b(k kVar) {
                if (a(kVar)) {
                    long c = kVar.c(g.QUARTER_OF_YEAR);
                    if (c != 1) {
                        return c == 2 ? q.i(1L, 91L) : (c == 3 || c == 4) ? q.i(1L, 92L) : c();
                    }
                    long c2 = kVar.c(a.YEAR);
                    j$.time.chrono.e.a.getClass();
                    return j$.time.chrono.e.a(c2) ? q.i(1L, 91L) : q.i(1L, 90L);
                }
                throw new p("Unsupported field: DayOfQuarter");
            }

            @Override // j$.time.temporal.l
            public final q c() {
                return q.j(90L, 92L);
            }

            @Override // j$.time.temporal.l
            public final long d(k kVar) {
                int[] iArr;
                if (a(kVar)) {
                    int e = kVar.e(a.DAY_OF_YEAR);
                    int e2 = kVar.e(a.MONTH_OF_YEAR);
                    long c = kVar.c(a.YEAR);
                    iArr = g.a;
                    int i = (e2 - 1) / 3;
                    j$.time.chrono.e.a.getClass();
                    return e - iArr[i + (j$.time.chrono.e.a(c) ? 4 : 0)];
                }
                throw new p("Unsupported field: DayOfQuarter");
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "DayOfQuarter";
            }
        };
        DAY_OF_QUARTER = gVar;
        g gVar2 = new g() { // from class: j$.time.temporal.d
            @Override // j$.time.temporal.l
            public final boolean a(k kVar) {
                return kVar.b(a.MONTH_OF_YEAR) && g.e(kVar);
            }

            @Override // j$.time.temporal.l
            public final q c() {
                return q.i(1L, 4L);
            }

            @Override // j$.time.temporal.l
            public final long d(k kVar) {
                if (a(kVar)) {
                    return (kVar.c(a.MONTH_OF_YEAR) + 2) / 3;
                }
                throw new p("Unsupported field: QuarterOfYear");
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "QuarterOfYear";
            }
        };
        QUARTER_OF_YEAR = gVar2;
        g gVar3 = new g() { // from class: j$.time.temporal.e
            @Override // j$.time.temporal.l
            public final boolean a(k kVar) {
                return kVar.b(a.EPOCH_DAY) && g.e(kVar);
            }

            @Override // j$.time.temporal.g, j$.time.temporal.l
            public final q b(k kVar) {
                q k;
                if (a(kVar)) {
                    k = g.k(LocalDate.h(kVar));
                    return k;
                }
                throw new p("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // j$.time.temporal.l
            public final q c() {
                return q.j(52L, 53L);
            }

            @Override // j$.time.temporal.l
            public final long d(k kVar) {
                if (a(kVar)) {
                    return g.h(LocalDate.h(kVar));
                }
                throw new p("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekOfWeekBasedYear";
            }
        };
        WEEK_OF_WEEK_BASED_YEAR = gVar3;
        g gVar4 = new g() { // from class: j$.time.temporal.f
            @Override // j$.time.temporal.l
            public final boolean a(k kVar) {
                return kVar.b(a.EPOCH_DAY) && g.e(kVar);
            }

            @Override // j$.time.temporal.l
            public final q c() {
                return a.YEAR.c();
            }

            @Override // j$.time.temporal.l
            public final long d(k kVar) {
                int j;
                if (a(kVar)) {
                    j = g.j(LocalDate.h(kVar));
                    return j;
                }
                throw new p("Unsupported field: WeekBasedYear");
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekBasedYear";
            }
        };
        WEEK_BASED_YEAR = gVar4;
        b = new g[]{gVar, gVar2, gVar3, gVar4};
        a = new int[]{0, 90, 181, 273, 0, 91, 182, 274};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(String str, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean e(k kVar) {
        if (kVar != null) {
            Object obj = (j$.time.chrono.d) kVar.d(j.b);
            if (obj == null) {
                obj = j$.time.chrono.e.a;
            }
            return ((j$.time.chrono.a) obj).equals(j$.time.chrono.e.a);
        }
        throw new NullPointerException("temporal");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0049, code lost:
        if ((r0 == -3 || (r0 == -2 && r5.m())) == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int h(LocalDate localDate) {
        int ordinal = localDate.j().ordinal();
        int i = 1;
        int k = localDate.k() - 1;
        int i2 = (3 - ordinal) + k;
        int i3 = (i2 - ((i2 / 7) * 7)) - 3;
        if (i3 < -3) {
            i3 += 7;
        }
        if (k < i3) {
            return (int) k(localDate.t().p(-1L)).d();
        }
        int i4 = ((k - i3) / 7) + 1;
        if (i4 == 53) {
        }
        i = i4;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int j(LocalDate localDate) {
        int l = localDate.l();
        int k = localDate.k();
        if (k <= 3) {
            return k - localDate.j().ordinal() < -2 ? l - 1 : l;
        } else if (k >= 363) {
            return ((k - 363) - (localDate.m() ? 1 : 0)) - localDate.j().ordinal() >= 0 ? l + 1 : l;
        } else {
            return l;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static q k(LocalDate localDate) {
        LocalDate of = LocalDate.of(j(localDate), 1, 1);
        return q.i(1L, (of.j() == DayOfWeek.THURSDAY || (of.j() == DayOfWeek.WEDNESDAY && of.m())) ? 53 : 52);
    }

    public static g valueOf(String str) {
        return (g) Enum.valueOf(g.class, str);
    }

    public static g[] values() {
        return (g[]) b.clone();
    }

    public q b(k kVar) {
        return c();
    }

    @Override // j$.time.temporal.l
    public final boolean isDateBased() {
        return true;
    }

    @Override // j$.time.temporal.l
    public final boolean isTimeBased() {
        return false;
    }
}
