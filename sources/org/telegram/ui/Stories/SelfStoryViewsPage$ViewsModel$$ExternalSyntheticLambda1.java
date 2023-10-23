package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.tl.TL_stories$TL_storyView;
import org.telegram.ui.Stories.SelfStoryViewsPage;
/* loaded from: classes4.dex */
public final /* synthetic */ class SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda1 INSTANCE = new SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda1();

    private /* synthetic */ SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda1() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$applyLocalFilter$2;
        lambda$applyLocalFilter$2 = SelfStoryViewsPage.ViewsModel.lambda$applyLocalFilter$2((TL_stories$TL_storyView) obj);
        return lambda$applyLocalFilter$2;
    }
}
