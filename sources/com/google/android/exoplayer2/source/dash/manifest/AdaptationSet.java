package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class AdaptationSet {
    public final List accessibilityDescriptors;
    public final List essentialProperties;
    public final long id;
    public final List representations;
    public final List supplementalProperties;
    public final int type;

    public AdaptationSet(long j, int i, List list, List list2, List list3, List list4) {
        this.id = j;
        this.type = i;
        this.representations = Collections.unmodifiableList(list);
        this.accessibilityDescriptors = Collections.unmodifiableList(list2);
        this.essentialProperties = Collections.unmodifiableList(list3);
        this.supplementalProperties = Collections.unmodifiableList(list4);
    }
}
