package j$.time.zone;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;
/* loaded from: classes2.dex */
final class e extends f {
    private final Set c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (String str : TimeZone.getAvailableIDs()) {
            linkedHashSet.add(str);
        }
        this.c = Collections.unmodifiableSet(linkedHashSet);
    }

    @Override // j$.time.zone.f
    protected final ZoneRules b(String str) {
        if (this.c.contains(str)) {
            return new ZoneRules(TimeZone.getTimeZone(str));
        }
        throw new c("Not a built-in time zone: " + str);
    }

    @Override // j$.time.zone.f
    protected final Set c() {
        return this.c;
    }
}
