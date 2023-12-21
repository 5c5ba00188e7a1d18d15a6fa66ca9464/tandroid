package org.telegram.ui.Stories;

import j$.util.function.ToIntFunction;
import org.telegram.tgnet.tl.TL_stories$StoryView;
import org.telegram.ui.Stories.SelfStoryViewsPage;
/* loaded from: classes4.dex */
public final /* synthetic */ class SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda2 implements ToIntFunction {
    public static final /* synthetic */ SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda2 INSTANCE = new SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda2();

    private /* synthetic */ SelfStoryViewsPage$ViewsModel$$ExternalSyntheticLambda2() {
    }

    @Override // j$.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$applyLocalFilter$4;
        lambda$applyLocalFilter$4 = SelfStoryViewsPage.ViewsModel.lambda$applyLocalFilter$4((TL_stories$StoryView) obj);
        return lambda$applyLocalFilter$4;
    }
}
