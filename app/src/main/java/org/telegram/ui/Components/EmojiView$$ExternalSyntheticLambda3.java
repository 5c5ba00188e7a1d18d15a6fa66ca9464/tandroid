package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$$ExternalSyntheticLambda3 implements View.OnTouchListener {
    public final /* synthetic */ EmojiView f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ EmojiView$$ExternalSyntheticLambda3(EmojiView emojiView, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = emojiView;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$new$2;
        lambda$new$2 = this.f$0.lambda$new$2(this.f$1, view, motionEvent);
        return lambda$new$2;
    }
}
