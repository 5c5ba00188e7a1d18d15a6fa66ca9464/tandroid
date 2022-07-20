package org.telegram.ui.Components;

import androidx.core.view.inputmethod.InputContentInfoCompat;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$12$$ExternalSyntheticLambda4 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass12 f$0;
    public final /* synthetic */ InputContentInfoCompat f$1;

    public /* synthetic */ ChatActivityEnterView$12$$ExternalSyntheticLambda4(ChatActivityEnterView.AnonymousClass12 anonymousClass12, InputContentInfoCompat inputContentInfoCompat) {
        this.f$0 = anonymousClass12;
        this.f$1 = inputContentInfoCompat;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$onCreateInputConnection$0(this.f$1, z, i);
    }
}
