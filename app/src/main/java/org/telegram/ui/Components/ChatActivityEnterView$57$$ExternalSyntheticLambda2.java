package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$57$$ExternalSyntheticLambda2 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass57 f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ Object f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ Object f$4;

    public /* synthetic */ ChatActivityEnterView$57$$ExternalSyntheticLambda2(ChatActivityEnterView.AnonymousClass57 anonymousClass57, View view, Object obj, String str, Object obj2) {
        this.f$0 = anonymousClass57;
        this.f$1 = view;
        this.f$2 = obj;
        this.f$3 = str;
        this.f$4 = obj2;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$onGifSelected$1(this.f$1, this.f$2, this.f$3, this.f$4, z, i);
    }
}
