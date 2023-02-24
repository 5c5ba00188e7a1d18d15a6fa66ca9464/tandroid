package com.google.android.exoplayer2.text.webvtt;

import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class WebvttSubtitle$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ WebvttSubtitle$$ExternalSyntheticLambda0 INSTANCE = new WebvttSubtitle$$ExternalSyntheticLambda0();

    private /* synthetic */ WebvttSubtitle$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getCues$0;
        lambda$getCues$0 = WebvttSubtitle.lambda$getCues$0((WebvttCueInfo) obj, (WebvttCueInfo) obj2);
        return lambda$getCues$0;
    }
}
