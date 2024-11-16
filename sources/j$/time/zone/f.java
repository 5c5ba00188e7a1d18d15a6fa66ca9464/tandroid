package j$.time.zone;

import j$.util.concurrent.ConcurrentHashMap;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public abstract class f {
    private static final CopyOnWriteArrayList a;
    private static final ConcurrentHashMap b;

    static {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        a = copyOnWriteArrayList;
        b = new ConcurrentHashMap(512, 0.75f, 2);
        ArrayList arrayList = new ArrayList();
        AccessController.doPrivileged(new d(arrayList));
        copyOnWriteArrayList.addAll(arrayList);
    }

    protected f() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ZoneRules a(String str, boolean z) {
        j$.util.a.B(str, "zoneId");
        ConcurrentHashMap concurrentHashMap = b;
        f fVar = (f) concurrentHashMap.get(str);
        if (fVar != null) {
            return fVar.b(str);
        }
        if (concurrentHashMap.isEmpty()) {
            throw new c("No time-zone data files registered");
        }
        throw new c("Unknown time-zone ID: ".concat(str));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void d(f fVar) {
        j$.util.a.B(fVar, "provider");
        for (String str : fVar.c()) {
            j$.util.a.B(str, "zoneId");
            if (((f) b.putIfAbsent(str, fVar)) != null) {
                throw new c("Unable to register zone as one already registered with that ID: " + str + ", currently loading from provider: " + fVar);
            }
        }
        a.add(fVar);
    }

    protected abstract ZoneRules b(String str);

    protected abstract Set c();
}
