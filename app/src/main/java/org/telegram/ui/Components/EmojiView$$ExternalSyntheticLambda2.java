package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$$ExternalSyntheticLambda2 implements View.OnTouchListener {
    public final /* synthetic */ EmojiView f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ EmojiView$$ExternalSyntheticLambda2(EmojiView emojiView, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = emojiView;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$new$1;
        lambda$new$1 = this.f$0.lambda$new$1(this.f$1, view, motionEvent);
        return lambda$new$1;
    }
}
