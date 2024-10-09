package j$.time.format;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class u {
    private final Map a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u(Map map) {
        Comparator comparator;
        Comparator comparator2;
        this.a = map;
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : map.entrySet()) {
            HashMap hashMap2 = new HashMap();
            for (Map.Entry entry2 : ((Map) entry.getValue()).entrySet()) {
                String str = (String) entry2.getValue();
                String str2 = (String) entry2.getValue();
                Long l = (Long) entry2.getKey();
                int i = v.c;
                hashMap2.put(str, new AbstractMap.SimpleImmutableEntry(str2, l));
            }
            ArrayList arrayList2 = new ArrayList(hashMap2.values());
            comparator2 = v.b;
            Collections.sort(arrayList2, comparator2);
            hashMap.put((TextStyle) entry.getKey(), arrayList2);
            arrayList.addAll(arrayList2);
            hashMap.put(null, arrayList);
        }
        comparator = v.b;
        Collections.sort(arrayList, comparator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String a(long j, TextStyle textStyle) {
        Map map = (Map) this.a.get(textStyle);
        if (map != null) {
            return (String) map.get(Long.valueOf(j));
        }
        return null;
    }
}
