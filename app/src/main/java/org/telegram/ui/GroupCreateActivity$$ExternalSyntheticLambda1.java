package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.Cells.CheckBoxCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateActivity$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ GroupCreateActivity f$0;
    public final /* synthetic */ CheckBoxCell[] f$1;

    public /* synthetic */ GroupCreateActivity$$ExternalSyntheticLambda1(GroupCreateActivity groupCreateActivity, CheckBoxCell[] checkBoxCellArr) {
        this.f$0 = groupCreateActivity;
        this.f$1 = checkBoxCellArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onDonePressed$6(this.f$1, dialogInterface, i);
    }
}
