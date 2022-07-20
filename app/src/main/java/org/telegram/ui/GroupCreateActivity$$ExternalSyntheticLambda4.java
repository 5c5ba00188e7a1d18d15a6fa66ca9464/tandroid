package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Cells.CheckBoxCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateActivity$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ CheckBoxCell[] f$0;

    public /* synthetic */ GroupCreateActivity$$ExternalSyntheticLambda4(CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = checkBoxCellArr;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        GroupCreateActivity.lambda$onDonePressed$5(this.f$0, view);
    }
}
