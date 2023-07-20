package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.TLRPC$TL_userStories;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesStorage$$ExternalSyntheticLambda14 implements ToIntFunction {
    public static final /* synthetic */ StoriesStorage$$ExternalSyntheticLambda14 INSTANCE = new StoriesStorage$$ExternalSyntheticLambda14();

    private /* synthetic */ StoriesStorage$$ExternalSyntheticLambda14() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$getAllStories$0;
        lambda$getAllStories$0 = StoriesStorage.lambda$getAllStories$0((TLRPC$TL_userStories) obj);
        return lambda$getAllStories$0;
    }
}
