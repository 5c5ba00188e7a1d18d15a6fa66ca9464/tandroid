package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$12$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass12 f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ PhotoViewer$12$$ExternalSyntheticLambda2(PhotoViewer.AnonymousClass12 anonymousClass12, boolean[] zArr) {
        this.f$0 = anonymousClass12;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$6(this.f$1, dialogInterface, i);
    }
}
