package com.google.android.exoplayer2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class CopyOnWriteMultiset implements Iterable {
    private final Object lock = new Object();
    private final Map elementCounts = new HashMap();
    private Set elementSet = Collections.emptySet();
    private List elements = Collections.emptyList();

    public void add(Object obj) {
        synchronized (this.lock) {
            try {
                ArrayList arrayList = new ArrayList(this.elements);
                arrayList.add(obj);
                this.elements = Collections.unmodifiableList(arrayList);
                Integer num = (Integer) this.elementCounts.get(obj);
                if (num == null) {
                    HashSet hashSet = new HashSet(this.elementSet);
                    hashSet.add(obj);
                    this.elementSet = Collections.unmodifiableSet(hashSet);
                }
                this.elementCounts.put(obj, Integer.valueOf(num != null ? 1 + num.intValue() : 1));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int count(Object obj) {
        int intValue;
        synchronized (this.lock) {
            try {
                intValue = this.elementCounts.containsKey(obj) ? ((Integer) this.elementCounts.get(obj)).intValue() : 0;
            } catch (Throwable th) {
                throw th;
            }
        }
        return intValue;
    }

    public Set elementSet() {
        Set set;
        synchronized (this.lock) {
            set = this.elementSet;
        }
        return set;
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        Iterator it;
        synchronized (this.lock) {
            it = this.elements.iterator();
        }
        return it;
    }

    public void remove(Object obj) {
        synchronized (this.lock) {
            try {
                Integer num = (Integer) this.elementCounts.get(obj);
                if (num == null) {
                    return;
                }
                ArrayList arrayList = new ArrayList(this.elements);
                arrayList.remove(obj);
                this.elements = Collections.unmodifiableList(arrayList);
                if (num.intValue() == 1) {
                    this.elementCounts.remove(obj);
                    HashSet hashSet = new HashSet(this.elementSet);
                    hashSet.remove(obj);
                    this.elementSet = Collections.unmodifiableSet(hashSet);
                } else {
                    this.elementCounts.put(obj, Integer.valueOf(num.intValue() - 1));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
