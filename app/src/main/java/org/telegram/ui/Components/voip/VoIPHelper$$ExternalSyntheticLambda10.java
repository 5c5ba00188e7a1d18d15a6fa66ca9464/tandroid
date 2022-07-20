package org.telegram.ui.Components.voip;

import android.content.SharedPreferences;
import android.view.View;
import org.telegram.ui.Cells.TextCheckCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPHelper$$ExternalSyntheticLambda10 implements View.OnClickListener {
    public final /* synthetic */ SharedPreferences f$0;
    public final /* synthetic */ TextCheckCell f$1;

    public /* synthetic */ VoIPHelper$$ExternalSyntheticLambda10(SharedPreferences sharedPreferences, TextCheckCell textCheckCell) {
        this.f$0 = sharedPreferences;
        this.f$1 = textCheckCell;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        VoIPHelper.lambda$showCallDebugSettings$18(this.f$0, this.f$1, view);
    }
}
