package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda6 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatMessageCell f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ float f$6;
    public final /* synthetic */ float f$7;
    public final /* synthetic */ float f$8;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda6(ChatMessageCell chatMessageCell, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.f$0 = chatMessageCell;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
        this.f$4 = f4;
        this.f$5 = f5;
        this.f$6 = f6;
        this.f$7 = f7;
        this.f$8 = f8;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatListItemAnimator.lambda$animateAddImpl$7(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, valueAnimator);
    }
}
