package org.telegram.ui.Components;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes4.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda97 implements TextView.OnEditorActionListener {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda97 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda97();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda97() {
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean hideKeyboard;
        hideKeyboard = AndroidUtilities.hideKeyboard(textView);
        return hideKeyboard;
    }
}
