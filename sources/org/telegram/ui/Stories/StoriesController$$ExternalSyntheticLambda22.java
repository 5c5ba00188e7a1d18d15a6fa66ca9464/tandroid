package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda22 implements ToIntFunction {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda22 INSTANCE = new StoriesController$$ExternalSyntheticLambda22();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda22() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int i;
        i = ((TL_stories$StoryItem) obj).date;
        return i;
    }
}
