package kotlin.collections;

import java.util.Collections;
import java.util.Map;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.tgnet.ConnectionsManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class MapsKt__MapsJVMKt extends MapsKt__MapWithDefaultKt {
    public static int mapCapacity(int i) {
        return i < 0 ? i : i < 3 ? i + 1 : i < 1073741824 ? (int) ((i / 0.75f) + 1.0f) : ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    public static final Map mapOf(Pair pair) {
        Intrinsics.checkNotNullParameter(pair, "pair");
        Map singletonMap = Collections.singletonMap(pair.getFirst(), pair.getSecond());
        Intrinsics.checkNotNullExpressionValue(singletonMap, "singletonMap(pair.first, pair.second)");
        return singletonMap;
    }

    public static final Map toSingletonMap(Map map) {
        Intrinsics.checkNotNullParameter(map, "<this>");
        Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
        Map singletonMap = Collections.singletonMap(entry.getKey(), entry.getValue());
        Intrinsics.checkNotNullExpressionValue(singletonMap, "with(entries.iterator().…ingletonMap(key, value) }");
        return singletonMap;
    }
}
