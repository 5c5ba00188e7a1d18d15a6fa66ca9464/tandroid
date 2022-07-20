package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.ChatListItemAnimator;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatListItemAnimator.MoveInfoExtended f$0;
    public final /* synthetic */ ChatMessageCell.TransitionParams f$1;
    public final /* synthetic */ ChatMessageCell f$2;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda0(ChatListItemAnimator.MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell) {
        this.f$0 = moveInfoExtended;
        this.f$1 = transitionParams;
        this.f$2 = chatMessageCell;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatListItemAnimator.lambda$animateMoveImpl$3(this.f$0, this.f$1, this.f$2, valueAnimator);
    }
}
