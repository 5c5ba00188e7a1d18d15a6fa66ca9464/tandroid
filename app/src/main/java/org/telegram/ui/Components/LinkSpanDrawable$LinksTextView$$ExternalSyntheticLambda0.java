package org.telegram.ui.Components;

import android.text.style.ClickableSpan;
import org.telegram.ui.Components.LinkSpanDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkSpanDrawable$LinksTextView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LinkSpanDrawable.LinksTextView f$0;
    public final /* synthetic */ ClickableSpan[] f$1;

    public /* synthetic */ LinkSpanDrawable$LinksTextView$$ExternalSyntheticLambda0(LinkSpanDrawable.LinksTextView linksTextView, ClickableSpan[] clickableSpanArr) {
        this.f$0 = linksTextView;
        this.f$1 = clickableSpanArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTouchEvent$0(this.f$1);
    }
}
