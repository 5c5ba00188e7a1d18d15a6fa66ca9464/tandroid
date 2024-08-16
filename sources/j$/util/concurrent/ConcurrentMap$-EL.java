package j$.util.concurrent;

import java.util.concurrent.ConcurrentMap;
/* loaded from: classes2.dex */
public final /* synthetic */ class ConcurrentMap$-EL {
    public static Object getOrDefault(ConcurrentMap concurrentMap, Object obj, Object obj2) {
        if (concurrentMap instanceof t) {
            return ((t) concurrentMap).getOrDefault(obj, obj2);
        }
        Object obj3 = concurrentMap.get(obj);
        return obj3 != null ? obj3 : obj2;
    }
}
