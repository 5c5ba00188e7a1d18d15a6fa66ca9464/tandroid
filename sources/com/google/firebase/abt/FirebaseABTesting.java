package com.google.firebase.abt;

import android.content.Context;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.inject.Provider;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class FirebaseABTesting {
    private final Provider analyticsConnector;
    private Integer maxUserProperties = null;
    private final String originService;

    public FirebaseABTesting(Context context, Provider provider, String str) {
        this.analyticsConnector = provider;
        this.originService = str;
    }

    private void addExperimentToAnalytics(AnalyticsConnector.ConditionalUserProperty conditionalUserProperty) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.analyticsConnector.get());
        throw null;
    }

    private void addExperiments(List list) {
        ArrayDeque arrayDeque = new ArrayDeque(getAllExperimentsInAnalytics());
        int maxUserPropertiesInAnalytics = getMaxUserPropertiesInAnalytics();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            AbtExperimentInfo abtExperimentInfo = (AbtExperimentInfo) it.next();
            while (arrayDeque.size() >= maxUserPropertiesInAnalytics) {
                removeExperimentFromAnalytics(((AnalyticsConnector.ConditionalUserProperty) arrayDeque.pollFirst()).name);
            }
            AnalyticsConnector.ConditionalUserProperty conditionalUserProperty = abtExperimentInfo.toConditionalUserProperty(this.originService);
            addExperimentToAnalytics(conditionalUserProperty);
            arrayDeque.offer(conditionalUserProperty);
        }
    }

    private static List convertMapsToExperimentInfos(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(AbtExperimentInfo.fromMap((Map) it.next()));
        }
        return arrayList;
    }

    private List getAllExperimentsInAnalytics() {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.analyticsConnector.get());
        throw null;
    }

    private ArrayList getExperimentsToAdd(List list, Set set) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            AbtExperimentInfo abtExperimentInfo = (AbtExperimentInfo) it.next();
            if (!set.contains(abtExperimentInfo.getExperimentId())) {
                arrayList.add(abtExperimentInfo);
            }
        }
        return arrayList;
    }

    private ArrayList getExperimentsToRemove(List list, Set set) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            AnalyticsConnector.ConditionalUserProperty conditionalUserProperty = (AnalyticsConnector.ConditionalUserProperty) it.next();
            if (!set.contains(conditionalUserProperty.name)) {
                arrayList.add(conditionalUserProperty);
            }
        }
        return arrayList;
    }

    private int getMaxUserPropertiesInAnalytics() {
        Integer num = this.maxUserProperties;
        if (num != null) {
            return num.intValue();
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.analyticsConnector.get());
        throw null;
    }

    private void removeExperimentFromAnalytics(String str) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.analyticsConnector.get());
        throw null;
    }

    private void removeExperiments(Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            removeExperimentFromAnalytics(((AnalyticsConnector.ConditionalUserProperty) it.next()).name);
        }
    }

    private void replaceAllExperimentsWith(List list) {
        if (list.isEmpty()) {
            removeAllExperiments();
            return;
        }
        HashSet hashSet = new HashSet();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            hashSet.add(((AbtExperimentInfo) it.next()).getExperimentId());
        }
        List<AnalyticsConnector.ConditionalUserProperty> allExperimentsInAnalytics = getAllExperimentsInAnalytics();
        HashSet hashSet2 = new HashSet();
        for (AnalyticsConnector.ConditionalUserProperty conditionalUserProperty : allExperimentsInAnalytics) {
            hashSet2.add(conditionalUserProperty.name);
        }
        removeExperiments(getExperimentsToRemove(allExperimentsInAnalytics, hashSet));
        addExperiments(getExperimentsToAdd(list, hashSet2));
    }

    private void throwAbtExceptionIfAnalyticsIsNull() {
        if (this.analyticsConnector.get() == null) {
            throw new AbtException("The Analytics SDK is not available. Please check that the Analytics SDK is included in your app dependencies.");
        }
    }

    public void removeAllExperiments() {
        throwAbtExceptionIfAnalyticsIsNull();
        removeExperiments(getAllExperimentsInAnalytics());
    }

    public void replaceAllExperiments(List list) {
        throwAbtExceptionIfAnalyticsIsNull();
        if (list == null) {
            throw new IllegalArgumentException("The replacementExperiments list is null.");
        }
        replaceAllExperimentsWith(convertMapsToExperimentInfos(list));
    }
}
