package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.ChatListItemAnimator;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ MessageObject.GroupedMessages.TransitionParams f$0;
    public final /* synthetic */ ChatListItemAnimator.MoveInfoExtended f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ RecyclerListView f$5;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda3(MessageObject.GroupedMessages.TransitionParams transitionParams, ChatListItemAnimator.MoveInfoExtended moveInfoExtended, boolean z, float f, float f2, RecyclerListView recyclerListView) {
        this.f$0 = transitionParams;
        this.f$1 = moveInfoExtended;
        this.f$2 = z;
        this.f$3 = f;
        this.f$4 = f2;
        this.f$5 = recyclerListView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatListItemAnimator.lambda$animateMoveImpl$4(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, valueAnimator);
    }
}
