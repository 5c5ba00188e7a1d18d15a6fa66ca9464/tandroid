package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ThemeActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeActivity$ListAdapter$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ThemeActivity.ListAdapter f$0;
    public final /* synthetic */ Theme.ThemeAccent f$1;
    public final /* synthetic */ ThemeActivity.ThemeAccentsListAdapter f$2;

    public /* synthetic */ ThemeActivity$ListAdapter$$ExternalSyntheticLambda0(ThemeActivity.ListAdapter listAdapter, Theme.ThemeAccent themeAccent, ThemeActivity.ThemeAccentsListAdapter themeAccentsListAdapter) {
        this.f$0 = listAdapter;
        this.f$1 = themeAccent;
        this.f$2 = themeAccentsListAdapter;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateViewHolder$4(this.f$1, this.f$2, dialogInterface, i);
    }
}
