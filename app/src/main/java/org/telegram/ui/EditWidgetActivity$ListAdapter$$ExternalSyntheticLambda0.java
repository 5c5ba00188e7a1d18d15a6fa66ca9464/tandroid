package org.telegram.ui;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.EditWidgetActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0 implements View.OnTouchListener {
    public final /* synthetic */ EditWidgetActivity.ListAdapter f$0;
    public final /* synthetic */ GroupCreateUserCell f$1;

    public /* synthetic */ EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0(EditWidgetActivity.ListAdapter listAdapter, GroupCreateUserCell groupCreateUserCell) {
        this.f$0 = listAdapter;
        this.f$1 = groupCreateUserCell;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$onCreateViewHolder$0;
        lambda$onCreateViewHolder$0 = this.f$0.lambda$onCreateViewHolder$0(this.f$1, view, motionEvent);
        return lambda$onCreateViewHolder$0;
    }
}
