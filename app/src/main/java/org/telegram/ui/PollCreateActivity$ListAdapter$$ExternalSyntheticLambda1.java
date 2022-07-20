package org.telegram.ui;

import android.view.KeyEvent;
import android.view.View;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.PollCreateActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PollCreateActivity$ListAdapter$$ExternalSyntheticLambda1 implements View.OnKeyListener {
    public final /* synthetic */ PollEditTextCell f$0;

    public /* synthetic */ PollCreateActivity$ListAdapter$$ExternalSyntheticLambda1(PollEditTextCell pollEditTextCell) {
        this.f$0 = pollEditTextCell;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean lambda$onCreateViewHolder$2;
        lambda$onCreateViewHolder$2 = PollCreateActivity.ListAdapter.lambda$onCreateViewHolder$2(this.f$0, view, i, keyEvent);
        return lambda$onCreateViewHolder$2;
    }
}
