package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatListItemAnimator.MoveInfoExtended f$0;
    public final /* synthetic */ ChatMessageCell.TransitionParams f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ ChatMessageCell f$5;
    public final /* synthetic */ int[] f$6;
    public final /* synthetic */ RecyclerView.ViewHolder f$7;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda1(ChatListItemAnimator.MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, boolean z, float f, float f2, ChatMessageCell chatMessageCell, int[] iArr, RecyclerView.ViewHolder viewHolder) {
        this.f$0 = moveInfoExtended;
        this.f$1 = transitionParams;
        this.f$2 = z;
        this.f$3 = f;
        this.f$4 = f2;
        this.f$5 = chatMessageCell;
        this.f$6 = iArr;
        this.f$7 = viewHolder;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatListItemAnimator.lambda$animateMoveImpl$2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, valueAnimator);
    }
}
