package com.google.android.exoplayer2.source.dash;

import android.os.SystemClock;
import android.util.Pair;
import com.google.android.exoplayer2.source.dash.manifest.BaseUrl;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* loaded from: classes.dex */
public final class BaseUrlExclusionList {
    private final Map excludedPriorities;
    private final Map excludedServiceLocations;
    private final Random random;
    private final Map selectionsTaken;

    public BaseUrlExclusionList() {
        this(new Random());
    }

    BaseUrlExclusionList(Random random) {
        this.selectionsTaken = new HashMap();
        this.random = random;
        this.excludedServiceLocations = new HashMap();
        this.excludedPriorities = new HashMap();
    }

    private static void addExclusion(Object obj, long j, Map map) {
        if (map.containsKey(obj)) {
            j = Math.max(j, ((Long) Util.castNonNull((Long) map.get(obj))).longValue());
        }
        map.put(obj, Long.valueOf(j));
    }

    private List applyExclusions(List list) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        removeExpiredExclusions(elapsedRealtime, this.excludedServiceLocations);
        removeExpiredExclusions(elapsedRealtime, this.excludedPriorities);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            BaseUrl baseUrl = (BaseUrl) list.get(i);
            if (!this.excludedServiceLocations.containsKey(baseUrl.serviceLocation) && !this.excludedPriorities.containsKey(Integer.valueOf(baseUrl.priority))) {
                arrayList.add(baseUrl);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int compareBaseUrl(BaseUrl baseUrl, BaseUrl baseUrl2) {
        int compare = Integer.compare(baseUrl.priority, baseUrl2.priority);
        return compare != 0 ? compare : baseUrl.serviceLocation.compareTo(baseUrl2.serviceLocation);
    }

    public static int getPriorityCount(List list) {
        HashSet hashSet = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            hashSet.add(Integer.valueOf(((BaseUrl) list.get(i)).priority));
        }
        return hashSet.size();
    }

    private static void removeExpiredExclusions(long j, Map map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : map.entrySet()) {
            if (((Long) entry.getValue()).longValue() <= j) {
                arrayList.add(entry.getKey());
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            map.remove(arrayList.get(i));
        }
    }

    private BaseUrl selectWeighted(List list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            i += ((BaseUrl) list.get(i2)).weight;
        }
        int nextInt = this.random.nextInt(i);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            BaseUrl baseUrl = (BaseUrl) list.get(i4);
            i3 += baseUrl.weight;
            if (nextInt < i3) {
                return baseUrl;
            }
        }
        return (BaseUrl) Iterables.getLast(list);
    }

    public void exclude(BaseUrl baseUrl, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        addExclusion(baseUrl.serviceLocation, elapsedRealtime, this.excludedServiceLocations);
        int i = baseUrl.priority;
        if (i != Integer.MIN_VALUE) {
            addExclusion(Integer.valueOf(i), elapsedRealtime, this.excludedPriorities);
        }
    }

    public int getPriorityCountAfterExclusion(List list) {
        HashSet hashSet = new HashSet();
        List applyExclusions = applyExclusions(list);
        for (int i = 0; i < applyExclusions.size(); i++) {
            hashSet.add(Integer.valueOf(((BaseUrl) applyExclusions.get(i)).priority));
        }
        return hashSet.size();
    }

    public void reset() {
        this.excludedServiceLocations.clear();
        this.excludedPriorities.clear();
        this.selectionsTaken.clear();
    }

    public BaseUrl selectBaseUrl(List list) {
        Object obj;
        List applyExclusions = applyExclusions(list);
        if (applyExclusions.size() >= 2) {
            Collections.sort(applyExclusions, new Comparator() { // from class: com.google.android.exoplayer2.source.dash.BaseUrlExclusionList$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj2, Object obj3) {
                    int compareBaseUrl;
                    compareBaseUrl = BaseUrlExclusionList.compareBaseUrl((BaseUrl) obj2, (BaseUrl) obj3);
                    return compareBaseUrl;
                }
            });
            ArrayList arrayList = new ArrayList();
            int i = ((BaseUrl) applyExclusions.get(0)).priority;
            int i2 = 0;
            while (true) {
                if (i2 >= applyExclusions.size()) {
                    break;
                }
                BaseUrl baseUrl = (BaseUrl) applyExclusions.get(i2);
                if (i == baseUrl.priority) {
                    arrayList.add(new Pair(baseUrl.serviceLocation, Integer.valueOf(baseUrl.weight)));
                    i2++;
                } else if (arrayList.size() == 1) {
                    obj = applyExclusions.get(0);
                }
            }
            BaseUrl baseUrl2 = (BaseUrl) this.selectionsTaken.get(arrayList);
            if (baseUrl2 != null) {
                return baseUrl2;
            }
            BaseUrl selectWeighted = selectWeighted(applyExclusions.subList(0, arrayList.size()));
            this.selectionsTaken.put(arrayList, selectWeighted);
            return selectWeighted;
        }
        obj = Iterables.getFirst(applyExclusions, null);
        return (BaseUrl) obj;
    }
}
