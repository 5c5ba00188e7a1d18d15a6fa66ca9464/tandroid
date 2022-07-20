package org.telegram.ui.Components;

import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.messenger.MediaController;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda12 implements Comparator {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda12(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateAlbumsDropDown$8;
        lambda$updateAlbumsDropDown$8 = ChatAttachAlertPhotoLayout.lambda$updateAlbumsDropDown$8(this.f$0, (MediaController.AlbumEntry) obj, (MediaController.AlbumEntry) obj2);
        return lambda$updateAlbumsDropDown$8;
    }
}
