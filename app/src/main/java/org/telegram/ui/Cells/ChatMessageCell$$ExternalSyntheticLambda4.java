package org.telegram.ui.Cells;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatMessageCell$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ChatMessageCell f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda4(ChatMessageCell chatMessageCell, int i) {
        this.f$0 = chatMessageCell;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkBotButtonMotionEvent$4(this.f$1);
    }
}
