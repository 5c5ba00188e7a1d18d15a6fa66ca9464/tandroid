package j$.time.temporal;
/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum NANO_OF_SECOND uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:391)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:320)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes2.dex */
public final class a implements k {
    public static final a ALIGNED_DAY_OF_WEEK_IN_MONTH;
    public static final a ALIGNED_DAY_OF_WEEK_IN_YEAR;
    public static final a ALIGNED_WEEK_OF_MONTH;
    public static final a ALIGNED_WEEK_OF_YEAR;
    public static final a AMPM_OF_DAY;
    public static final a CLOCK_HOUR_OF_AMPM;
    public static final a CLOCK_HOUR_OF_DAY;
    public static final a DAY_OF_MONTH;
    public static final a DAY_OF_WEEK;
    public static final a DAY_OF_YEAR;
    public static final a EPOCH_DAY;
    public static final a ERA;
    public static final a HOUR_OF_AMPM;
    public static final a HOUR_OF_DAY;
    public static final a INSTANT_SECONDS;
    public static final a MICRO_OF_DAY;
    public static final a MICRO_OF_SECOND;
    public static final a MILLI_OF_DAY;
    public static final a MILLI_OF_SECOND;
    public static final a MINUTE_OF_DAY;
    public static final a MINUTE_OF_HOUR;
    public static final a MONTH_OF_YEAR;
    public static final a NANO_OF_DAY;
    public static final a NANO_OF_SECOND;
    public static final a OFFSET_SECONDS;
    public static final a PROLEPTIC_MONTH;
    public static final a SECOND_OF_DAY;
    public static final a SECOND_OF_MINUTE;
    public static final a YEAR;
    public static final a YEAR_OF_ERA;
    private static final /* synthetic */ a[] c;
    private final String a;
    private final n b;

    static {
        b bVar = b.NANOS;
        b bVar2 = b.SECONDS;
        a aVar = new a("NANO_OF_SECOND", 0, "NanoOfSecond", bVar, bVar2, n.c(0L, 999999999L));
        NANO_OF_SECOND = aVar;
        b bVar3 = b.DAYS;
        a aVar2 = new a("NANO_OF_DAY", 1, "NanoOfDay", bVar, bVar3, n.c(0L, 86399999999999L));
        NANO_OF_DAY = aVar2;
        b bVar4 = b.MICROS;
        a aVar3 = new a("MICRO_OF_SECOND", 2, "MicroOfSecond", bVar4, bVar2, n.c(0L, 999999L));
        MICRO_OF_SECOND = aVar3;
        a aVar4 = new a("MICRO_OF_DAY", 3, "MicroOfDay", bVar4, bVar3, n.c(0L, 86399999999L));
        MICRO_OF_DAY = aVar4;
        b bVar5 = b.MILLIS;
        a aVar5 = new a("MILLI_OF_SECOND", 4, "MilliOfSecond", bVar5, bVar2, n.c(0L, 999L));
        MILLI_OF_SECOND = aVar5;
        a aVar6 = new a("MILLI_OF_DAY", 5, "MilliOfDay", bVar5, bVar3, n.c(0L, 86399999L));
        MILLI_OF_DAY = aVar6;
        b bVar6 = b.MINUTES;
        a aVar7 = new a("SECOND_OF_MINUTE", 6, "SecondOfMinute", bVar2, bVar6, n.c(0L, 59L), "second");
        SECOND_OF_MINUTE = aVar7;
        a aVar8 = new a("SECOND_OF_DAY", 7, "SecondOfDay", bVar2, bVar3, n.c(0L, 86399L));
        SECOND_OF_DAY = aVar8;
        b bVar7 = b.HOURS;
        a aVar9 = new a("MINUTE_OF_HOUR", 8, "MinuteOfHour", bVar6, bVar7, n.c(0L, 59L), "minute");
        MINUTE_OF_HOUR = aVar9;
        a aVar10 = new a("MINUTE_OF_DAY", 9, "MinuteOfDay", bVar6, bVar3, n.c(0L, 1439L));
        MINUTE_OF_DAY = aVar10;
        b bVar8 = b.HALF_DAYS;
        a aVar11 = new a("HOUR_OF_AMPM", 10, "HourOfAmPm", bVar7, bVar8, n.c(0L, 11L));
        HOUR_OF_AMPM = aVar11;
        a aVar12 = new a("CLOCK_HOUR_OF_AMPM", 11, "ClockHourOfAmPm", bVar7, bVar8, n.c(1L, 12L));
        CLOCK_HOUR_OF_AMPM = aVar12;
        a aVar13 = new a("HOUR_OF_DAY", 12, "HourOfDay", bVar7, bVar3, n.c(0L, 23L), "hour");
        HOUR_OF_DAY = aVar13;
        a aVar14 = new a("CLOCK_HOUR_OF_DAY", 13, "ClockHourOfDay", bVar7, bVar3, n.c(1L, 24L));
        CLOCK_HOUR_OF_DAY = aVar14;
        a aVar15 = new a("AMPM_OF_DAY", 14, "AmPmOfDay", bVar8, bVar3, n.c(0L, 1L), "dayperiod");
        AMPM_OF_DAY = aVar15;
        b bVar9 = b.WEEKS;
        a aVar16 = new a("DAY_OF_WEEK", 15, "DayOfWeek", bVar3, bVar9, n.c(1L, 7L), "weekday");
        DAY_OF_WEEK = aVar16;
        a aVar17 = new a("ALIGNED_DAY_OF_WEEK_IN_MONTH", 16, "AlignedDayOfWeekInMonth", bVar3, bVar9, n.c(1L, 7L));
        ALIGNED_DAY_OF_WEEK_IN_MONTH = aVar17;
        a aVar18 = new a("ALIGNED_DAY_OF_WEEK_IN_YEAR", 17, "AlignedDayOfWeekInYear", bVar3, bVar9, n.c(1L, 7L));
        ALIGNED_DAY_OF_WEEK_IN_YEAR = aVar18;
        b bVar10 = b.MONTHS;
        a aVar19 = new a("DAY_OF_MONTH", 18, "DayOfMonth", bVar3, bVar10, n.d(1L, 28L, 31L), "day");
        DAY_OF_MONTH = aVar19;
        b bVar11 = b.YEARS;
        a aVar20 = new a("DAY_OF_YEAR", 19, "DayOfYear", bVar3, bVar11, n.d(1L, 365L, 366L));
        DAY_OF_YEAR = aVar20;
        b bVar12 = b.FOREVER;
        a aVar21 = new a("EPOCH_DAY", 20, "EpochDay", bVar3, bVar12, n.c(-365249999634L, 365249999634L));
        EPOCH_DAY = aVar21;
        a aVar22 = new a("ALIGNED_WEEK_OF_MONTH", 21, "AlignedWeekOfMonth", bVar9, bVar10, n.d(1L, 4L, 5L));
        ALIGNED_WEEK_OF_MONTH = aVar22;
        a aVar23 = new a("ALIGNED_WEEK_OF_YEAR", 22, "AlignedWeekOfYear", bVar9, bVar11, n.c(1L, 53L));
        ALIGNED_WEEK_OF_YEAR = aVar23;
        a aVar24 = new a("MONTH_OF_YEAR", 23, "MonthOfYear", bVar10, bVar11, n.c(1L, 12L), "month");
        MONTH_OF_YEAR = aVar24;
        a aVar25 = new a("PROLEPTIC_MONTH", 24, "ProlepticMonth", bVar10, bVar12, n.c(-11999999988L, 11999999999L));
        PROLEPTIC_MONTH = aVar25;
        a aVar26 = new a("YEAR_OF_ERA", 25, "YearOfEra", bVar11, bVar12, n.d(1L, 999999999L, 1000000000L));
        YEAR_OF_ERA = aVar26;
        a aVar27 = new a("YEAR", 26, "Year", bVar11, bVar12, n.c(-999999999L, 999999999L), "year");
        YEAR = aVar27;
        a aVar28 = new a("ERA", 27, "Era", b.ERAS, bVar12, n.c(0L, 1L), "era");
        ERA = aVar28;
        a aVar29 = new a("INSTANT_SECONDS", 28, "InstantSeconds", bVar2, bVar12, n.c(Long.MIN_VALUE, Long.MAX_VALUE));
        INSTANT_SECONDS = aVar29;
        a aVar30 = new a("OFFSET_SECONDS", 29, "OffsetSeconds", bVar2, bVar12, n.c(-64800L, 64800L));
        OFFSET_SECONDS = aVar30;
        c = new a[]{aVar, aVar2, aVar3, aVar4, aVar5, aVar6, aVar7, aVar8, aVar9, aVar10, aVar11, aVar12, aVar13, aVar14, aVar15, aVar16, aVar17, aVar18, aVar19, aVar20, aVar21, aVar22, aVar23, aVar24, aVar25, aVar26, aVar27, aVar28, aVar29, aVar30};
    }

    private a(String str, int i, String str2, m mVar, m mVar2, n nVar) {
        this.a = str2;
        this.b = nVar;
    }

    private a(String str, int i, String str2, m mVar, m mVar2, n nVar, String str3) {
        this.a = str2;
        this.b = nVar;
    }

    public static a valueOf(String str) {
        return (a) Enum.valueOf(a.class, str);
    }

    public static a[] values() {
        return (a[]) c.clone();
    }

    @Override // j$.time.temporal.k
    public n a() {
        return this.b;
    }

    public long b(long j) {
        this.b.a(j, this);
        return j;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.a;
    }
}
