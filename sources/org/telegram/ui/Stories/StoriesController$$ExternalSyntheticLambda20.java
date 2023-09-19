package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.TLRPC$StoryItem;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda20 implements ToIntFunction {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda20 INSTANCE = new StoriesController$$ExternalSyntheticLambda20();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda20() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int i;
        i = ((TLRPC$StoryItem) obj).date;
        return i;
    }
}
