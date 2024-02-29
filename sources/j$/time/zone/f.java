package j$.time.zone;

import j$.util.concurrent.ConcurrentHashMap;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
public abstract class f {
    private static final CopyOnWriteArrayList a;
    private static final ConcurrentMap b;

    static {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        a = copyOnWriteArrayList;
        b = new ConcurrentHashMap(LiteMode.FLAG_CALLS_ANIMATIONS, 0.75f, 2);
        ArrayList arrayList = new ArrayList();
        AccessController.doPrivileged(new d(arrayList));
        copyOnWriteArrayList.addAll(arrayList);
    }

    public static ZoneRules a(String str, boolean z) {
        Objects.requireNonNull(str, "zoneId");
        ConcurrentMap concurrentMap = b;
        f fVar = (f) concurrentMap.get(str);
        if (fVar == null) {
            if (concurrentMap.isEmpty()) {
                throw new c("No time-zone data files registered");
            }
            throw new c("Unknown time-zone ID: " + str);
        }
        return fVar.b(str, z);
    }

    public static void d(f fVar) {
        Objects.requireNonNull(fVar, "provider");
        for (String str : fVar.c()) {
            Objects.requireNonNull(str, "zoneId");
            if (((f) b.putIfAbsent(str, fVar)) != null) {
                throw new c("Unable to register zone as one already registered with that ID: " + str + ", currently loading from provider: " + fVar);
            }
        }
        a.add(fVar);
    }

    protected abstract ZoneRules b(String str, boolean z);

    protected abstract Set c();
}
