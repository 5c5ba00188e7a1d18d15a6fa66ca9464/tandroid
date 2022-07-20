package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.EditWidgetActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditWidgetActivity$2$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ EditWidgetActivity.AnonymousClass2 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ EditWidgetActivity$2$$ExternalSyntheticLambda0(EditWidgetActivity.AnonymousClass2 anonymousClass2, int i) {
        this.f$0 = anonymousClass2;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$0(this.f$1, dialogInterface, i);
    }
}
