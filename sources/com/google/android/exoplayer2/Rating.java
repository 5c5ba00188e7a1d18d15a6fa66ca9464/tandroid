package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public abstract class Rating implements Bundleable {
    static final String FIELD_RATING_TYPE = Util.intToStringMaxRadix(0);
    public static final Bundleable.Creator CREATOR = new Bundleable.Creator() { // from class: com.google.android.exoplayer2.Rating$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.Bundleable.Creator
        public final Bundleable fromBundle(Bundle bundle) {
            Rating fromBundle;
            fromBundle = Rating.fromBundle(bundle);
            return fromBundle;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static Rating fromBundle(Bundle bundle) {
        Bundleable.Creator creator;
        int i = bundle.getInt(FIELD_RATING_TYPE, -1);
        if (i == 0) {
            creator = HeartRating.CREATOR;
        } else if (i == 1) {
            creator = PercentageRating.CREATOR;
        } else if (i == 2) {
            creator = StarRating.CREATOR;
        } else if (i != 3) {
            throw new IllegalArgumentException("Unknown RatingType: " + i);
        } else {
            creator = ThumbRating.CREATOR;
        }
        return (Rating) creator.fromBundle(bundle);
    }
}
