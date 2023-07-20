package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.TLRPC$StoryItem;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda13 implements ToIntFunction {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda13 INSTANCE = new StoriesController$$ExternalSyntheticLambda13();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda13() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int i;
        i = ((TLRPC$StoryItem) obj).date;
        return i;
    }
}
