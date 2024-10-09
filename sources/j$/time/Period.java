package j$.time;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public final class Period implements Serializable {
    public static final Period d = new Period(0, 0, 0);
    private final int a;
    private final int b;
    private final int c;

    static {
        Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", 2);
        Collections.unmodifiableList(Arrays.asList(j$.time.temporal.b.YEARS, j$.time.temporal.b.MONTHS, j$.time.temporal.b.DAYS));
    }

    private Period(int i, int i2, int i3) {
        this.a = i;
        this.b = i2;
        this.c = i3;
    }

    public static Period a(int i, int i2, int i3) {
        return ((i | i2) | i3) == 0 ? d : new Period(i, i2, i3);
    }

    public static Period between(LocalDate localDate, LocalDate localDate2) {
        return localDate.t(localDate2);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Period)) {
            return false;
        }
        Period period = (Period) obj;
        return this.a == period.a && this.b == period.b && this.c == period.c;
    }

    public int getYears() {
        return this.a;
    }

    public final int hashCode() {
        return Integer.rotateLeft(this.c, 16) + Integer.rotateLeft(this.b, 8) + this.a;
    }

    public final String toString() {
        if (this == d) {
            return "P0D";
        }
        StringBuilder sb = new StringBuilder("P");
        int i = this.a;
        if (i != 0) {
            sb.append(i);
            sb.append('Y');
        }
        int i2 = this.b;
        if (i2 != 0) {
            sb.append(i2);
            sb.append('M');
        }
        int i3 = this.c;
        if (i3 != 0) {
            sb.append(i3);
            sb.append('D');
        }
        return sb.toString();
    }
}
