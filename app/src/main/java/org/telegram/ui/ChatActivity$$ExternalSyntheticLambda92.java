package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Cells.CheckBoxCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda92 implements View.OnClickListener {
    public final /* synthetic */ CheckBoxCell[] f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda92(CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = checkBoxCellArr;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChatActivity.lambda$showRequestUrlAlert$229(this.f$0, view);
    }
}
