package j$.time.temporal;

import j$.time.DayOfWeek;
import j$.time.LocalDate;
import org.telegram.messenger.NotificationCenter;

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
            public final q a() {
                return q.j(90L, 92L);
            }

            @Override // j$.time.temporal.l
            public final long b(k kVar) {
                int[] iArr;
                if (!c(kVar)) {
                    throw new p("Unsupported field: DayOfQuarter");
                }
                int d = kVar.d(a.DAY_OF_YEAR);
                int d2 = kVar.d(a.MONTH_OF_YEAR);
                long b2 = kVar.b(a.YEAR);
                iArr = g.a;
                int i = (d2 - 1) / 3;
                j$.time.chrono.g.a.getClass();
                return d - iArr[i + (j$.time.chrono.g.a(b2) ? 4 : 0)];
            }

            @Override // j$.time.temporal.l
            public final boolean c(k kVar) {
                return kVar.e(a.DAY_OF_YEAR) && kVar.e(a.MONTH_OF_YEAR) && kVar.e(a.YEAR) && g.f(kVar);
            }

            @Override // j$.time.temporal.g, j$.time.temporal.l
            public final q d(k kVar) {
                if (!c(kVar)) {
                    throw new p("Unsupported field: DayOfQuarter");
                }
                long b2 = kVar.b(g.QUARTER_OF_YEAR);
                if (b2 != 1) {
                    return b2 == 2 ? q.i(1L, 91L) : (b2 == 3 || b2 == 4) ? q.i(1L, 92L) : a();
                }
                long b3 = kVar.b(a.YEAR);
                j$.time.chrono.g.a.getClass();
                return j$.time.chrono.g.a(b3) ? q.i(1L, 91L) : q.i(1L, 90L);
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "DayOfQuarter";
            }
        };
        DAY_OF_QUARTER = gVar;
        g gVar2 = new g() { // from class: j$.time.temporal.d
            @Override // j$.time.temporal.l
            public final q a() {
                return q.i(1L, 4L);
            }

            @Override // j$.time.temporal.l
            public final long b(k kVar) {
                if (c(kVar)) {
                    return (kVar.b(a.MONTH_OF_YEAR) + 2) / 3;
                }
                throw new p("Unsupported field: QuarterOfYear");
            }

            @Override // j$.time.temporal.l
            public final boolean c(k kVar) {
                return kVar.e(a.MONTH_OF_YEAR) && g.f(kVar);
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "QuarterOfYear";
            }
        };
        QUARTER_OF_YEAR = gVar2;
        g gVar3 = new g() { // from class: j$.time.temporal.e
            @Override // j$.time.temporal.l
            public final q a() {
                return q.j(52L, 53L);
            }

            @Override // j$.time.temporal.l
            public final long b(k kVar) {
                if (c(kVar)) {
                    return g.i(LocalDate.h(kVar));
                }
                throw new p("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // j$.time.temporal.l
            public final boolean c(k kVar) {
                return kVar.e(a.EPOCH_DAY) && g.f(kVar);
            }

            @Override // j$.time.temporal.g, j$.time.temporal.l
            public final q d(k kVar) {
                q l;
                if (!c(kVar)) {
                    throw new p("Unsupported field: WeekOfWeekBasedYear");
                }
                l = g.l(LocalDate.h(kVar));
                return l;
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekOfWeekBasedYear";
            }
        };
        WEEK_OF_WEEK_BASED_YEAR = gVar3;
        g gVar4 = new g() { // from class: j$.time.temporal.f
            @Override // j$.time.temporal.l
            public final q a() {
                return a.YEAR.a();
            }

            @Override // j$.time.temporal.l
            public final long b(k kVar) {
                int k;
                if (!c(kVar)) {
                    throw new p("Unsupported field: WeekBasedYear");
                }
                k = g.k(LocalDate.h(kVar));
                return k;
            }

            @Override // j$.time.temporal.l
            public final boolean c(k kVar) {
                return kVar.e(a.EPOCH_DAY) && g.f(kVar);
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekBasedYear";
            }
        };
        WEEK_BASED_YEAR = gVar4;
        b = new g[]{gVar, gVar2, gVar3, gVar4};
        a = new int[]{0, 90, NotificationCenter.giftsToUserSent, NotificationCenter.chatAvailableReactionsUpdated, 0, 91, NotificationCenter.didStartedMultiGiftsSelector, NotificationCenter.dialogsUnreadReactionsCounterChanged};
    }

    g(String str, int i) {
    }

    static boolean f(k kVar) {
        j$.util.a.B(kVar, "temporal");
        Object obj = (j$.time.chrono.f) kVar.c(j.b);
        if (obj == null) {
            obj = j$.time.chrono.g.a;
        }
        return ((j$.time.chrono.a) obj).equals(j$.time.chrono.g.a);
    }

    static int i(LocalDate localDate) {
        int ordinal = localDate.j().ordinal();
        int i = 1;
        int k = localDate.k() - 1;
        int i2 = (3 - ordinal) + k;
        int i3 = i2 - ((i2 / 7) * 7);
        int i4 = i3 - 3;
        if (i4 < -3) {
            i4 = i3 + 4;
        }
        if (k < i4) {
            return (int) l(localDate.u().q(-1L)).d();
        }
        int i5 = ((k - i4) / 7) + 1;
        if (i5 != 53 || i4 == -3 || (i4 == -2 && localDate.n())) {
            i = i5;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int k(LocalDate localDate) {
        int m = localDate.m();
        int k = localDate.k();
        if (k <= 3) {
            return k - localDate.j().ordinal() < -2 ? m - 1 : m;
        }
        if (k >= 363) {
            return ((k - 363) - (localDate.n() ? 1 : 0)) - localDate.j().ordinal() >= 0 ? m + 1 : m;
        }
        return m;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static q l(LocalDate localDate) {
        LocalDate of = LocalDate.of(k(localDate), 1, 1);
        return q.i(1L, (of.j() == DayOfWeek.THURSDAY || (of.j() == DayOfWeek.WEDNESDAY && of.n())) ? 53 : 52);
    }

    public static g valueOf(String str) {
        return (g) Enum.valueOf(g.class, str);
    }

    public static g[] values() {
        return (g[]) b.clone();
    }

    public q d(k kVar) {
        return a();
    }

    @Override // j$.time.temporal.l
    public final boolean e() {
        return true;
    }
}
