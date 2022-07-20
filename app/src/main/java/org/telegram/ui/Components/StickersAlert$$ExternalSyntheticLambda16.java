package org.telegram.ui.Components;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda16 implements TextView.OnEditorActionListener {
    public final /* synthetic */ AlertDialog.Builder f$0;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda16(AlertDialog.Builder builder) {
        this.f$0 = builder;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$showNameEnterAlert$23;
        lambda$showNameEnterAlert$23 = StickersAlert.lambda$showNameEnterAlert$23(this.f$0, textView, i, keyEvent);
        return lambda$showNameEnterAlert$23;
    }
}
