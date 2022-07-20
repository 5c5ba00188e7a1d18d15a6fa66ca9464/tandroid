package org.telegram.ui.Components.Reactions;

import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactionsLayoutInBubble$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ReactionsLayoutInBubble f$0;
    public final /* synthetic */ ReactionsLayoutInBubble.ReactionButton f$1;

    public /* synthetic */ ReactionsLayoutInBubble$$ExternalSyntheticLambda0(ReactionsLayoutInBubble reactionsLayoutInBubble, ReactionsLayoutInBubble.ReactionButton reactionButton) {
        this.f$0 = reactionsLayoutInBubble;
        this.f$1 = reactionButton;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$chekTouchEvent$1(this.f$1);
    }
}
