package j$.time.temporal;
/* loaded from: classes2.dex */
public enum a implements l {
    NANO_OF_SECOND("NanoOfSecond", q.i(0, 999999999)),
    NANO_OF_DAY("NanoOfDay", q.i(0, 86399999999999L)),
    MICRO_OF_SECOND("MicroOfSecond", q.i(0, 999999)),
    MICRO_OF_DAY("MicroOfDay", q.i(0, 86399999999L)),
    MILLI_OF_SECOND("MilliOfSecond", q.i(0, 999)),
    MILLI_OF_DAY("MilliOfDay", q.i(0, 86399999)),
    SECOND_OF_MINUTE("SecondOfMinute", q.i(0, 59), 0),
    SECOND_OF_DAY("SecondOfDay", q.i(0, 86399)),
    MINUTE_OF_HOUR("MinuteOfHour", q.i(0, 59), 0),
    MINUTE_OF_DAY("MinuteOfDay", q.i(0, 1439)),
    HOUR_OF_AMPM("HourOfAmPm", q.i(0, 11)),
    CLOCK_HOUR_OF_AMPM("ClockHourOfAmPm", q.i(1, 12)),
    HOUR_OF_DAY("HourOfDay", q.i(0, 23), 0),
    CLOCK_HOUR_OF_DAY("ClockHourOfDay", q.i(1, 24)),
    AMPM_OF_DAY("AmPmOfDay", q.i(0, 1), 0),
    DAY_OF_WEEK("DayOfWeek", q.i(1, 7), 0),
    ALIGNED_DAY_OF_WEEK_IN_MONTH("AlignedDayOfWeekInMonth", q.i(1, 7)),
    ALIGNED_DAY_OF_WEEK_IN_YEAR("AlignedDayOfWeekInYear", q.i(1, 7)),
    DAY_OF_MONTH("DayOfMonth", q.j(28, 31), 0),
    DAY_OF_YEAR("DayOfYear", q.j(365, 366)),
    EPOCH_DAY("EpochDay", q.i(-365249999634L, 365249999634L)),
    ALIGNED_WEEK_OF_MONTH("AlignedWeekOfMonth", q.j(4, 5)),
    ALIGNED_WEEK_OF_YEAR("AlignedWeekOfYear", q.i(1, 53)),
    MONTH_OF_YEAR("MonthOfYear", q.i(1, 12), 0),
    PROLEPTIC_MONTH("ProlepticMonth", q.i(-11999999988L, 11999999999L)),
    YEAR_OF_ERA("YearOfEra", q.j(999999999, 1000000000)),
    YEAR("Year", q.i(-999999999, 999999999), 0),
    ERA("Era", q.i(0, 1), 0),
    INSTANT_SECONDS("InstantSeconds", q.i(Long.MIN_VALUE, Long.MAX_VALUE)),
    OFFSET_SECONDS("OffsetSeconds", q.i(-64800, 64800));
    
    private final String a;
    private final q b;

    static {
        b bVar = b.NANOS;
    }

    a(String str, q qVar) {
        this.a = str;
        this.b = qVar;
    }

    a(String str, q qVar, int i) {
        this.a = str;
        this.b = qVar;
    }

    @Override // j$.time.temporal.l
    public final boolean a(k kVar) {
        return kVar.b(this);
    }

    @Override // j$.time.temporal.l
    public final q b(k kVar) {
        return kVar.a(this);
    }

    @Override // j$.time.temporal.l
    public final q c() {
        return this.b;
    }

    @Override // j$.time.temporal.l
    public final long d(k kVar) {
        return kVar.c(this);
    }

    public final int e(long j) {
        return this.b.a(j, this);
    }

    public final void f(long j) {
        this.b.b(j, this);
    }

    @Override // j$.time.temporal.l
    public final boolean isDateBased() {
        return ordinal() >= DAY_OF_WEEK.ordinal() && ordinal() <= ERA.ordinal();
    }

    @Override // j$.time.temporal.l
    public final boolean isTimeBased() {
        return ordinal() < DAY_OF_WEEK.ordinal();
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
