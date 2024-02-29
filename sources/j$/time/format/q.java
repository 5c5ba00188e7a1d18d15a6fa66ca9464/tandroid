package j$.time.format;

import j$.time.Instant;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.util.concurrent.ConcurrentHashMap;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q extends p {
    private static final Map d = new ConcurrentHashMap();
    private final TextStyle c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public q(TextStyle textStyle, Set set) {
        super(r4, "ZoneText(" + textStyle + ")");
        int i = j$.time.temporal.t.a;
        j$.time.temporal.q qVar = j$.time.temporal.q.a;
        new HashMap();
        new HashMap();
        Objects.requireNonNull(textStyle, "textStyle");
        this.c = textStyle;
    }

    @Override // j$.time.format.p, j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        String[] strArr;
        int i = j$.time.temporal.t.a;
        ZoneId zoneId = (ZoneId) tVar.f(j$.time.temporal.m.a);
        if (zoneId == null) {
            return false;
        }
        String id = zoneId.getId();
        if (!(zoneId instanceof ZoneOffset)) {
            j$.time.temporal.k d2 = tVar.d();
            char c = d2.e(j$.time.temporal.a.INSTANT_SECONDS) ? zoneId.getRules().c(Instant.h(d2)) ? (char) 1 : (char) 0 : (char) 2;
            Locale c2 = tVar.c();
            String str = null;
            Map map = null;
            if (this.c != TextStyle.NARROW) {
                Map map2 = d;
                SoftReference softReference = (SoftReference) map2.get(id);
                if (softReference == null || (map = (Map) softReference.get()) == null || (strArr = (String[]) map.get(c2)) == null) {
                    TimeZone timeZone = TimeZone.getTimeZone(id);
                    String[] strArr2 = {id, timeZone.getDisplayName(false, 1, c2), timeZone.getDisplayName(false, 0, c2), timeZone.getDisplayName(true, 1, c2), timeZone.getDisplayName(true, 0, c2), id, id};
                    if (map == null) {
                        map = new ConcurrentHashMap();
                    }
                    map.put(c2, strArr2);
                    map2.put(id, new SoftReference(map));
                    strArr = strArr2;
                }
                int a = this.c.a();
                str = c != 0 ? c != 1 ? strArr[a + 5] : strArr[a + 3] : strArr[a + 1];
            }
            if (str != null) {
                id = str;
            }
        }
        sb.append(id);
        return true;
    }
}
