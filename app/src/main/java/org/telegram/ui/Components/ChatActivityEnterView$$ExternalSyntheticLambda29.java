package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$$ExternalSyntheticLambda29 implements View.OnTouchListener {
    public final /* synthetic */ ChatActivityEnterView f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatActivityEnterView$$ExternalSyntheticLambda29(ChatActivityEnterView chatActivityEnterView, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$new$24;
        lambda$new$24 = this.f$0.lambda$new$24(this.f$1, view, motionEvent);
        return lambda$new$24;
    }
}
