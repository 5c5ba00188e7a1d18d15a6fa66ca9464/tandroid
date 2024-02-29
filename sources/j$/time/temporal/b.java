package j$.time.temporal;
/* loaded from: classes2.dex */
public enum b implements v {
    NANOS("Nanos", j$.time.d.b(1)),
    MICROS("Micros", j$.time.d.b(1000)),
    MILLIS("Millis", j$.time.d.b(1000000)),
    SECONDS("Seconds", j$.time.d.c(1)),
    MINUTES("Minutes", j$.time.d.c(60)),
    HOURS("Hours", j$.time.d.c(3600)),
    HALF_DAYS("HalfDays", j$.time.d.c(43200)),
    DAYS("Days", j$.time.d.c(86400)),
    WEEKS("Weeks", j$.time.d.c(604800)),
    MONTHS("Months", j$.time.d.c(2629746)),
    YEARS("Years", j$.time.d.c(31556952)),
    DECADES("Decades", j$.time.d.c(315569520)),
    CENTURIES("Centuries", j$.time.d.c(3155695200L)),
    MILLENNIA("Millennia", j$.time.d.c(31556952000L)),
    ERAS("Eras", j$.time.d.c(31556952000000000L)),
    FOREVER("Forever", j$.time.d.d(Long.MAX_VALUE, 999999999));
    
    private final String a;

    b(String str, j$.time.d dVar) {
        this.a = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.a;
    }
}
