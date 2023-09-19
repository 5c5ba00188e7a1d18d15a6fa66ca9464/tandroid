package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.TLRPC$PeerStories;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesStorage$$ExternalSyntheticLambda15 implements ToIntFunction {
    public static final /* synthetic */ StoriesStorage$$ExternalSyntheticLambda15 INSTANCE = new StoriesStorage$$ExternalSyntheticLambda15();

    private /* synthetic */ StoriesStorage$$ExternalSyntheticLambda15() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$getAllStories$1;
        lambda$getAllStories$1 = StoriesStorage.lambda$getAllStories$1((TLRPC$PeerStories) obj);
        return lambda$getAllStories$1;
    }
}
