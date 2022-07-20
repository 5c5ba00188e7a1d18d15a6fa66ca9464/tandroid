package org.telegram.ui;

import android.content.DialogInterface;
import java.util.ArrayList;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$12$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass12 f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ PhotoViewer$12$$ExternalSyntheticLambda1(PhotoViewer.AnonymousClass12 anonymousClass12, ArrayList arrayList) {
        this.f$0 = anonymousClass12;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$2(this.f$1, dialogInterface, i);
    }
}
