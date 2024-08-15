package j$.time;

import j$.time.format.TextStyle;
import j$.time.format.r;
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
        if (getClass() != ZoneOffset.class && getClass() != o.class) {
            throw new AssertionError("Invalid subclass");
        }
    }

    public static ZoneId f(String str, ZoneOffset zoneOffset) {
        if (str != null) {
            if (zoneOffset != null) {
                if (str.length() == 0) {
                    return zoneOffset;
                }
                if (str.equals("GMT") || str.equals("UTC") || str.equals("UT")) {
                    if (zoneOffset.getTotalSeconds() != 0) {
                        str = str.concat(zoneOffset.getId());
                    }
                    return new o(str, ZoneRules.d(zoneOffset));
                }
                throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: ".concat(str));
            }
            throw new NullPointerException("offset");
        }
        throw new NullPointerException("prefix");
    }

    public static ZoneId of(String str) {
        int i;
        if (str != null) {
            if (str.length() <= 1 || str.startsWith("+") || str.startsWith("-")) {
                return ZoneOffset.g(str);
            }
            if (str.startsWith("UTC") || str.startsWith("GMT")) {
                i = 3;
            } else if (!str.startsWith("UT")) {
                return o.g(str);
            } else {
                i = 2;
            }
            String substring = str.substring(0, i);
            if (str.length() == i) {
                return f(substring, ZoneOffset.f);
            }
            if (str.charAt(i) == '+' || str.charAt(i) == '-') {
                try {
                    ZoneOffset g = ZoneOffset.g(str.substring(i));
                    ZoneOffset zoneOffset = ZoneOffset.f;
                    return f(substring, g);
                } catch (d e) {
                    throw new d("Invalid ID for offset-based ZoneId: ".concat(str), e);
                }
            }
            return o.g(str);
        }
        throw new NullPointerException("zoneId");
    }

    public static ZoneId systemDefault() {
        String id = TimeZone.getDefault().getID();
        if (id != null) {
            Map map = a;
            if (map != null) {
                String str = (String) map.get(id);
                if (str != null) {
                    id = str;
                }
                return of(id);
            }
            throw new NullPointerException("aliasMap");
        }
        throw new NullPointerException("zoneId");
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
        r rVar = new r();
        rVar.o(textStyle);
        return rVar.v(locale).a(new n(this));
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
