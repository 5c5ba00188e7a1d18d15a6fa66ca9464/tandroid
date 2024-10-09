package com.google.common.collect;

import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public interface Multimap {
    Map asMap();

    void clear();

    boolean containsEntry(Object obj, Object obj2);

    Collection entries();

    Collection get(Object obj);

    boolean put(Object obj, Object obj2);

    boolean remove(Object obj, Object obj2);

    int size();

    Collection values();
}
