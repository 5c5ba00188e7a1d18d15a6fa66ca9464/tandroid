package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$52$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass52 f$0;

    public /* synthetic */ PhotoViewer$52$$ExternalSyntheticLambda1(PhotoViewer.AnonymousClass52 anonymousClass52) {
        this.f$0 = anonymousClass52;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onError$0(dialogInterface, i);
    }
}
