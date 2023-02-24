package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.dash.manifest.BaseUrl;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class BaseUrlExclusionList$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ BaseUrlExclusionList$$ExternalSyntheticLambda0 INSTANCE = new BaseUrlExclusionList$$ExternalSyntheticLambda0();

    private /* synthetic */ BaseUrlExclusionList$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int compareBaseUrl;
        compareBaseUrl = BaseUrlExclusionList.compareBaseUrl((BaseUrl) obj, (BaseUrl) obj2);
        return compareBaseUrl;
    }
}
