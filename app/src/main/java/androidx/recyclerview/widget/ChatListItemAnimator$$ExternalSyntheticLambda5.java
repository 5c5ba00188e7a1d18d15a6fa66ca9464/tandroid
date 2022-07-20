package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda5 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatMessageCell.TransitionParams f$0;
    public final /* synthetic */ ChatMessageCell f$1;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda5(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell) {
        this.f$0 = transitionParams;
        this.f$1 = chatMessageCell;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatListItemAnimator.lambda$animateMoveImpl$5(this.f$0, this.f$1, valueAnimator);
    }
}
