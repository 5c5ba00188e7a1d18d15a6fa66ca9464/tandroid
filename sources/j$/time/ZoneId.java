package j$.time;

import j$.time.format.TextStyle;
import j$.time.format.q;
import j$.time.zone.ZoneRules;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public abstract class ZoneId implements Serializable {
    public static final Map a;

    static {
        HashMap hashMap = new HashMap(64);
        hashMap.put("ACT", "Australia/Darwin");
        hashMap.put("AET", "Australia/Sydney");
        hashMap.put("AGT", "America/Argentina/Buenos_Aires");
        hashMap.put("ART", "Africa/Cairo");
        hashMap.put("AST", "America/Anchorage");
        hashMap.put("BET", "America/Sao_Paulo");
        hashMap.put("BST", "Asia/Dhaka");
        hashMap.put("CAT", "Africa/Harare");
        hashMap.put("CNT", "America/St_Johns");
        hashMap.put("CST", "America/Chicago");
        hashMap.put("CTT", "Asia/Shanghai");
        hashMap.put("EAT", "Africa/Addis_Ababa");
        hashMap.put("ECT", "Europe/Paris");
        hashMap.put("IET", "America/Indiana/Indianapolis");
        hashMap.put("IST", "Asia/Kolkata");
        hashMap.put("JST", "Asia/Tokyo");
        hashMap.put("MIT", "Pacific/Apia");
        hashMap.put("NET", "Asia/Yerevan");
        hashMap.put("NST", "Pacific/Auckland");
        hashMap.put("PLT", "Asia/Karachi");
        hashMap.put("PNT", "America/Phoenix");
        hashMap.put("PRT", "America/Puerto_Rico");
        hashMap.put("PST", "America/Los_Angeles");
        hashMap.put("SST", "Pacific/Guadalcanal");
        hashMap.put("VST", "Asia/Ho_Chi_Minh");
        hashMap.put("EST", "-05:00");
        hashMap.put("MST", "-07:00");
        hashMap.put("HST", "-10:00");
        a = Collections.unmodifiableMap(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZoneId() {
        if (getClass() != ZoneOffset.class && getClass() != n.class) {
            throw new AssertionError("Invalid subclass");
        }
    }

    public static ZoneId f(String str, ZoneOffset zoneOffset) {
        j$.util.a.B(str, "prefix");
        j$.util.a.B(zoneOffset, "offset");
        if (str.length() == 0) {
            return zoneOffset;
        }
        if (!str.equals("GMT") && !str.equals("UTC") && !str.equals("UT")) {
            throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: ".concat(str));
        }
        if (zoneOffset.getTotalSeconds() != 0) {
            str = str.concat(zoneOffset.getId());
        }
        return new n(str, ZoneRules.e(zoneOffset));
    }

    private static ZoneId g(String str, int i) {
        String substring = str.substring(0, i);
        if (str.length() == i) {
            return f(substring, ZoneOffset.f);
        }
        if (str.charAt(i) != '+' && str.charAt(i) != '-') {
            return n.h(str);
        }
        try {
            ZoneOffset h = ZoneOffset.h(str.substring(i));
            return h == ZoneOffset.f ? f(substring, h) : f(substring, h);
        } catch (c e) {
            throw new c("Invalid ID for offset-based ZoneId: ".concat(str), e);
        }
    }

    public static ZoneId of(String str) {
        int i;
        j$.util.a.B(str, "zoneId");
        if (str.length() <= 1 || str.startsWith("+") || str.startsWith("-")) {
            return ZoneOffset.h(str);
        }
        if (str.startsWith("UTC") || str.startsWith("GMT")) {
            i = 3;
        } else {
            if (!str.startsWith("UT")) {
                return n.h(str);
            }
            i = 2;
        }
        return g(str, i);
    }

    public static ZoneId systemDefault() {
        String id = TimeZone.getDefault().getID();
        j$.util.a.B(id, "zoneId");
        Map map = a;
        j$.util.a.B(map, "aliasMap");
        String str = (String) map.get(id);
        if (str != null) {
            id = str;
        }
        return of(id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneId) {
            return getId().equals(((ZoneId) obj).getId());
        }
        return false;
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        q qVar = new q();
        qVar.o(textStyle);
        return qVar.v(locale).a(new m(this));
    }

    public abstract String getId();

    public abstract ZoneRules getRules();

    public int hashCode() {
        return getId().hashCode();
    }

    public String toString() {
        return getId();
    }
}
