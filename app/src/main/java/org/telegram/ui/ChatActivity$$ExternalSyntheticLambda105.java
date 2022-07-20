package org.telegram.ui;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ContentPreviewViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda105 implements View.OnTouchListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ ContentPreviewViewer.ContentPreviewViewerDelegate f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda105(ChatActivity chatActivity, ContentPreviewViewer.ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
        this.f$0 = chatActivity;
        this.f$1 = contentPreviewViewerDelegate;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$createView$42;
        lambda$createView$42 = this.f$0.lambda$createView$42(this.f$1, view, motionEvent);
        return lambda$createView$42;
    }
}
